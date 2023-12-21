package com.deg.finalproject.controller;

import com.deg.finalproject.dto.ComprobanteDTO;
import com.deg.finalproject.entidad.Comprobante;
import com.deg.finalproject.service.ComprobanteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/comprobante")
public class ComprobanteController {

    @Autowired
    private ComprobanteService comprobanteService;

    @GetMapping
    public List<ComprobanteDTO> findAll() {
        return this.comprobanteService.findAll();
    }

    // Single item
    @GetMapping("/{id}")
    public ComprobanteDTO one(@PathVariable Integer id) {

        return this.comprobanteService.findById(id);
    }

    @PostMapping
    public ComprobanteDTO newEntity(@RequestBody Comprobante comprobante) {
        return this.comprobanteService.save(comprobante);
    }}
