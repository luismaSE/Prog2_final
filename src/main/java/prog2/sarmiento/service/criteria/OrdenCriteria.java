package prog2.sarmiento.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import prog2.sarmiento.domain.enumeration.Estado;
import prog2.sarmiento.domain.enumeration.Modo;
import prog2.sarmiento.domain.enumeration.Operacion;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link prog2.sarmiento.domain.Orden} entity. This class is used
 * in {@link prog2.sarmiento.web.rest.OrdenResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /ordens?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrdenCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Operacion
     */
    public static class OperacionFilter extends Filter<Operacion> {

        public OperacionFilter() {}

        public OperacionFilter(OperacionFilter filter) {
            super(filter);
        }

        @Override
        public OperacionFilter copy() {
            return new OperacionFilter(this);
        }
    }

    /**
     * Class for filtering Modo
     */
    public static class ModoFilter extends Filter<Modo> {

        public ModoFilter() {}

        public ModoFilter(ModoFilter filter) {
            super(filter);
        }

        @Override
        public ModoFilter copy() {
            return new ModoFilter(this);
        }
    }

    /**
     * Class for filtering Estado
     */
    public static class EstadoFilter extends Filter<Estado> {

        public EstadoFilter() {}

        public EstadoFilter(EstadoFilter filter) {
            super(filter);
        }

        @Override
        public EstadoFilter copy() {
            return new EstadoFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter cliente;

    private IntegerFilter accionId;

    private StringFilter accion;

    private OperacionFilter operacion;

    private DoubleFilter precio;

    private IntegerFilter cantidad;

    private ModoFilter modo;

    private EstadoFilter estado;

    private StringFilter descripcionEstado;

    private InstantFilter fechaOperacion;

    private Boolean distinct;

    public OrdenCriteria() {}

    public OrdenCriteria(OrdenCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.cliente = other.cliente == null ? null : other.cliente.copy();
        this.accionId = other.accionId == null ? null : other.accionId.copy();
        this.accion = other.accion == null ? null : other.accion.copy();
        this.operacion = other.operacion == null ? null : other.operacion.copy();
        this.precio = other.precio == null ? null : other.precio.copy();
        this.cantidad = other.cantidad == null ? null : other.cantidad.copy();
        this.modo = other.modo == null ? null : other.modo.copy();
        this.estado = other.estado == null ? null : other.estado.copy();
        this.descripcionEstado = other.descripcionEstado == null ? null : other.descripcionEstado.copy();
        this.fechaOperacion = other.fechaOperacion == null ? null : other.fechaOperacion.copy();
        this.distinct = other.distinct;
    }

    @Override
    public OrdenCriteria copy() {
        return new OrdenCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getCliente() {
        return cliente;
    }

    public IntegerFilter cliente() {
        if (cliente == null) {
            cliente = new IntegerFilter();
        }
        return cliente;
    }

    public void setCliente(IntegerFilter cliente) {
        this.cliente = cliente;
    }

    public IntegerFilter getAccionId() {
        return accionId;
    }

    public IntegerFilter accionId() {
        if (accionId == null) {
            accionId = new IntegerFilter();
        }
        return accionId;
    }

    public void setAccionId(IntegerFilter accionId) {
        this.accionId = accionId;
    }

    public StringFilter getAccion() {
        return accion;
    }

    public StringFilter accion() {
        if (accion == null) {
            accion = new StringFilter();
        }
        return accion;
    }

    public void setAccion(StringFilter accion) {
        this.accion = accion;
    }

    public OperacionFilter getOperacion() {
        return operacion;
    }

    public OperacionFilter operacion() {
        if (operacion == null) {
            operacion = new OperacionFilter();
        }
        return operacion;
    }

    public void setOperacion(OperacionFilter operacion) {
        this.operacion = operacion;
    }

    public DoubleFilter getPrecio() {
        return precio;
    }

    public DoubleFilter precio() {
        if (precio == null) {
            precio = new DoubleFilter();
        }
        return precio;
    }

    public void setPrecio(DoubleFilter precio) {
        this.precio = precio;
    }

    public IntegerFilter getCantidad() {
        return cantidad;
    }

    public IntegerFilter cantidad() {
        if (cantidad == null) {
            cantidad = new IntegerFilter();
        }
        return cantidad;
    }

    public void setCantidad(IntegerFilter cantidad) {
        this.cantidad = cantidad;
    }

    public ModoFilter getModo() {
        return modo;
    }

    public ModoFilter modo() {
        if (modo == null) {
            modo = new ModoFilter();
        }
        return modo;
    }

    public void setModo(ModoFilter modo) {
        this.modo = modo;
    }

    public EstadoFilter getEstado() {
        return estado;
    }

    public EstadoFilter estado() {
        if (estado == null) {
            estado = new EstadoFilter();
        }
        return estado;
    }

    public void setEstado(EstadoFilter estado) {
        this.estado = estado;
    }

    public StringFilter getDescripcionEstado() {
        return descripcionEstado;
    }

    public StringFilter descripcionEstado() {
        if (descripcionEstado == null) {
            descripcionEstado = new StringFilter();
        }
        return descripcionEstado;
    }

    public void setDescripcionEstado(StringFilter descripcionEstado) {
        this.descripcionEstado = descripcionEstado;
    }

    public InstantFilter getFechaOperacion() {
        return fechaOperacion;
    }

    public InstantFilter fechaOperacion() {
        if (fechaOperacion == null) {
            fechaOperacion = new InstantFilter();
        }
        return fechaOperacion;
    }

    public void setFechaOperacion(InstantFilter fechaOperacion) {
        this.fechaOperacion = fechaOperacion;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OrdenCriteria that = (OrdenCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(cliente, that.cliente) &&
            Objects.equals(accionId, that.accionId) &&
            Objects.equals(accion, that.accion) &&
            Objects.equals(operacion, that.operacion) &&
            Objects.equals(precio, that.precio) &&
            Objects.equals(cantidad, that.cantidad) &&
            Objects.equals(modo, that.modo) &&
            Objects.equals(estado, that.estado) &&
            Objects.equals(descripcionEstado, that.descripcionEstado) &&
            Objects.equals(fechaOperacion, that.fechaOperacion) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            cliente,
            accionId,
            accion,
            operacion,
            precio,
            cantidad,
            modo,
            estado,
            descripcionEstado,
            fechaOperacion,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrdenCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (cliente != null ? "cliente=" + cliente + ", " : "") +
            (accionId != null ? "accionId=" + accionId + ", " : "") +
            (accion != null ? "accion=" + accion + ", " : "") +
            (operacion != null ? "operacion=" + operacion + ", " : "") +
            (precio != null ? "precio=" + precio + ", " : "") +
            (cantidad != null ? "cantidad=" + cantidad + ", " : "") +
            (modo != null ? "modo=" + modo + ", " : "") +
            (estado != null ? "estado=" + estado + ", " : "") +
            (descripcionEstado != null ? "descripcionEstado=" + descripcionEstado + ", " : "") +
            (fechaOperacion != null ? "fechaOperacion=" + fechaOperacion + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
