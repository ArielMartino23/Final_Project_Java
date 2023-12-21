package com.deg.finalproject.service;

import com.deg.finalproject.dto.ComprobanteDTO;
import com.deg.finalproject.dto.LineaDTO;
import com.deg.finalproject.entidad.Cliente;
import com.deg.finalproject.entidad.Comprobante;
import com.deg.finalproject.entidad.Linea;
import com.deg.finalproject.entidad.Producto;
import com.deg.finalproject.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Configuration
@Service
public class ComprobanteService {

    @Autowired
    private com.deg.finalproject.repository.ComprobanteRepository comprobanteRepository;

    @Autowired
    private com.deg.finalproject.repository.ClienteRepository clienteRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private RestTemplate restTemplate;

    public List<ComprobanteDTO> findAll() {
        return crearComprobantesDTO(this.comprobanteRepository.findAll());
    }

    public ComprobanteDTO save(Comprobante comprobante) {

        Boolean existeCliente = existeCliente(comprobante.getCliente());

        Boolean existenProductos = existenProductos(comprobante.getLineas());

        Boolean existeStock = existeStock(comprobante.getLineas());

        if (existeCliente && existenProductos && existeStock) {

            var comprobanteAGuardar = armarComprobante(comprobante);

            actualizarStock(comprobanteAGuardar.getLineas());

            return crearComprobanteDTO(this.comprobanteRepository.save(comprobanteAGuardar));
        } else {
            return new ComprobanteDTO();
        }
    }

    private void actualizarStock(Set<Linea> lineas) {
        for (Linea linea : lineas) {

            var cantidadVendida = linea.getCantidad();
            var producto = linea.getProducto();

            var productoDB = this.productoRepository.getById(producto.getProductoid());
            var stock = productoDB.getCantidad();
            var nuevoStock = stock - cantidadVendida;
            productoDB.setCantidad(nuevoStock);

            this.productoRepository.save(productoDB);

        }

    }

    public ComprobanteDTO findById(Integer id) {

        var opCliente =  this.comprobanteRepository.findById(id);
        if (opCliente.isPresent()) {
            return crearComprobanteDTO(opCliente.get());
        } else {
            return new ComprobanteDTO();
        }
    }

    private List<ComprobanteDTO> crearComprobantesDTO(List<Comprobante> comprobantes) {
        List<ComprobanteDTO> comprobantesDTOs = new ArrayList<ComprobanteDTO>();
        for (Comprobante comprobante : comprobantes) {
            comprobantesDTOs.add(this.crearComprobanteDTO(comprobante));
        }

        return comprobantesDTOs;
    }

    private ComprobanteDTO crearComprobanteDTO(Comprobante comprobante) {
        ComprobanteDTO dto = new ComprobanteDTO();

        dto.setComprobanteid(comprobante.getComprobanteid());

        dto.setCantidad(comprobante.getCantidad());

        dto.setFecha(comprobante.getFecha());

        dto.setTotal(comprobante.getTotal());

        dto.setCliente(comprobante.getCliente());

        dto.setLineas(crearLineasDTO(comprobante.getLineas()));


        return dto;
    }

    private Set<com.deg.finalproject.dto.LineaDTO> crearLineasDTO(Set<Linea> lineas) {
        Set<com.deg.finalproject.dto.LineaDTO> dtos = new HashSet<com.deg.finalproject.dto.LineaDTO>();

        for (Linea linea : lineas) {

            LineaDTO dto = new LineaDTO();

            dto.setLineaid(linea.getLineaid());

            dto.setCantidad(linea.getCantidad());

            dto.setDescripcion(linea.getDescripcion());

            dto.setPrecio(linea.getPrecio());

            dtos.add(dto);

        }

        return dtos;
    }

    private Comprobante armarComprobante(Comprobante comprobante) {

        var comprobanteAGuardar = new Comprobante();

        comprobanteAGuardar.setCliente(this.clienteRepository.findById( comprobante.getCliente().getClienteid() ).get());

        com.coder.house.ventas.online.ventasonline.entidad.WorldClock worldClock = this.restTemplate.getForObject("http://worldclockapi.com/api/json/utc/now", com.coder.house.ventas.online.ventasonline.entidad.WorldClock.class);

        String currentDateTime = worldClock.getCurrentDateTime();
        // "2021-12-08T17:36Z"
        try {
            Date date1=new SimpleDateFormat("yyyy-MM-dd'T'mm:ss'Z'").parse(currentDateTime);
            comprobanteAGuardar.setFecha(date1);
        } catch (ParseException e) {
            e.printStackTrace();
            comprobanteAGuardar.setFecha(new Date());
        }

        comprobanteAGuardar.setLineas(new HashSet<Linea>());
        for (Linea linea : comprobante.getLineas()) {
            comprobanteAGuardar.addLinea(crearLinea(linea));
        }

//		comprobanteAGuardar.setLineas(armarLineas(comprobante.getLineas(), comprobanteAGuardar));

        comprobanteAGuardar.setTotal(calcularTotal(comprobanteAGuardar.getLineas()));
        comprobanteAGuardar.setCantidad(comprobante.getLineas().size());

        return comprobanteAGuardar;
    }

    private BigDecimal calcularTotal(Set<Linea> lineas) {
        BigDecimal total = new BigDecimal("0");

        for (Linea linea : lineas) {
            //total = total.add(new BigDecimal(linea.getPrecio().toString()));
            total = total.add( linea.getPrecio().multiply(BigDecimal.valueOf(linea.getCantidad())) );
        }

        return total;
    }

    private Linea crearLinea(Linea linea) {
        Linea lineaAGuardar = new Linea();

        Producto productoDB = this.productoRepository.findById(linea.getProducto().getProductoid()).get();
        lineaAGuardar.setCantidad(linea.getCantidad());
        lineaAGuardar.setDescripcion(productoDB.getDescripcion());
        lineaAGuardar.setPrecio(productoDB.getPrecio());
        lineaAGuardar.setProducto(productoDB);

        return lineaAGuardar;
    }

    private Boolean existeStock(Set<Linea> lineas) {
        for (Linea linea : lineas) {
            var productoid = linea.getProducto().getProductoid();
            var opProducto = this.productoRepository.findById(productoid);
            if (opProducto.isEmpty()) {
                return false;
            }
            if (linea.getCantidad() < opProducto.get().getCantidad()) {
                return true;
            }
        }
        return false;
    }

    private Boolean existenProductos(Set<Linea> lineas) {
        for (Linea linea : lineas) {
            var productoId = linea.getProducto().getProductoid();
            var opProducto = this.productoRepository.findById(productoId);
            if (opProducto.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private Boolean existeCliente(Cliente cliente) {
        var opCliente = this.clienteRepository.findById(cliente.getClienteid());
        return !opCliente.isEmpty();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
