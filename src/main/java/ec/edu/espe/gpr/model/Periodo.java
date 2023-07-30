/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.edu.espe.gpr.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "periodo")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Periodo implements Serializable {

    private static final long serialVersionUID = 12345678L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CODIGO_PERIODO")
    private Integer codigoPeriodo;
    
    @Column(name = "NOMBRE_PERIODO", length = 256)
    private String nombrePeriodo;
    
    @Column(name = "DESCRIPCION_PERIODO")
    private String descripcionPeriodo;

    @Column(name = "ESTADO_PERIODO", length = 16)
    private String estadoPeriodo;

    @Column(name = "FECHA_CREACION_PERIODO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacionPeriodo;

    @Column(name = "FECHA_MODIFICACION_PERIODO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacionPeriodo;
}
