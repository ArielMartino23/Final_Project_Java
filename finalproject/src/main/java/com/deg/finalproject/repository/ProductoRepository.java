package com.deg.finalproject.repository;

import com.deg.finalproject.entidad.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

/*import com.coder.house.ventas.online.ventasonline.entidad.Producto;*/

public interface ProductoRepository extends JpaRepository<Producto, Integer> {

}
