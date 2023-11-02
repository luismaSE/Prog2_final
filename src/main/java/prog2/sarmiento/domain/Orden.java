package prog2.sarmiento.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import prog2.sarmiento.domain.enumeration.EstadoOrden;
import prog2.sarmiento.domain.enumeration.ModoOrden;
import prog2.sarmiento.domain.enumeration.Operacion;

/**
 * A Orden.
 */
@Entity
@Table(name = "orden")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Orden implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "cliente", nullable = false)
    private Long cliente;

    @NotNull
    @Column(name = "accion_id", nullable = false)
    private Long accionId;

    @NotNull
    @Column(name = "accion", nullable = false)
    private String accion;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "operacion", nullable = false)
    private Operacion operacion;

    @NotNull
    @Column(name = "precio", nullable = false)
    private Long precio;

    @NotNull
    @Column(name = "cantidad", nullable = false)
    private Long cantidad;

    @NotNull
    @Column(name = "fecha_operacion", nullable = false)
    private String fechaOperacion;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "modo", nullable = false)
    private ModoOrden modo;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoOrden estado;

    @Column(name = "descripcion_estado")
    private String descripcionEstado;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Orden id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCliente() {
        return this.cliente;
    }

    public Orden cliente(Long cliente) {
        this.setCliente(cliente);
        return this;
    }

    public void setCliente(Long cliente) {
        this.cliente = cliente;
    }

    public Long getAccionId() {
        return this.accionId;
    }

    public Orden accionId(Long accionId) {
        this.setAccionId(accionId);
        return this;
    }

    public void setAccionId(Long accionId) {
        this.accionId = accionId;
    }

    public String getAccion() {
        return this.accion;
    }

    public Orden accion(String accion) {
        this.setAccion(accion);
        return this;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public Operacion getOperacion() {
        return this.operacion;
    }

    public Orden operacion(Operacion operacion) {
        this.setOperacion(operacion);
        return this;
    }

    public void setOperacion(Operacion operacion) {
        this.operacion = operacion;
    }

    public Long getPrecio() {
        return this.precio;
    }

    public Orden precio(Long precio) {
        this.setPrecio(precio);
        return this;
    }

    public void setPrecio(Long precio) {
        this.precio = precio;
    }

    public Long getCantidad() {
        return this.cantidad;
    }

    public Orden cantidad(Long cantidad) {
        this.setCantidad(cantidad);
        return this;
    }

    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
    }

    public String getFechaOperacion() {
        return this.fechaOperacion;
    }

    public Orden fechaOperacion(String fechaOperacion) {
        this.setFechaOperacion(fechaOperacion);
        return this;
    }

    public void setFechaOperacion(String fechaOperacion) {
        this.fechaOperacion = fechaOperacion;
    }

    public ModoOrden getModo() {
        return this.modo;
    }

    public Orden modo(ModoOrden modo) {
        this.setModo(modo);
        return this;
    }

    public void setModo(ModoOrden modo) {
        this.modo = modo;
    }

    public EstadoOrden getEstado() {
        return this.estado;
    }

    public Orden estado(EstadoOrden estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(EstadoOrden estado) {
        this.estado = estado;
    }

    public String getDescripcionEstado() {
        return this.descripcionEstado;
    }

    public Orden descripcionEstado(String descripcionEstado) {
        this.setDescripcionEstado(descripcionEstado);
        return this;
    }

    public void setDescripcionEstado(String descripcionEstado) {
        this.descripcionEstado = descripcionEstado;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Orden)) {
            return false;
        }
        return id != null && id.equals(((Orden) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Orden{" +
            "id=" + getId() +
            ", cliente=" + getCliente() +
            ", accionId=" + getAccionId() +
            ", accion='" + getAccion() + "'" +
            ", operacion='" + getOperacion() + "'" +
            ", precio=" + getPrecio() +
            ", cantidad=" + getCantidad() +
            ", fechaOperacion='" + getFechaOperacion() + "'" +
            ", modo='" + getModo() + "'" +
            ", estado='" + getEstado() + "'" +
            ", descripcionEstado='" + getDescripcionEstado() + "'" +
            "}";
    }
}
