package todocode.hackacode.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.validation.Valid;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import todocode.hackacode.domain.Venta;
import todocode.hackacode.model.VentaDTO;
import todocode.hackacode.repos.VentaRepository;
import todocode.hackacode.service.impl.VentaServiceImpl;
import todocode.hackacode.util.NotFoundException;

@RestController
@RequestMapping(value = "/api/ventas", produces = MediaType.APPLICATION_JSON_VALUE)
public class VentaResource {

    private final VentaServiceImpl ventaServiceImpl;
    private final EntityManager entityManager;
    private final VentaRepository ventaRepository;

    @Autowired
    public VentaResource(VentaServiceImpl ventaServiceImpl, EntityManager entityManager, VentaRepository ventaRepository) {
        this.ventaServiceImpl = ventaServiceImpl;
        this.entityManager = entityManager;
        this.ventaRepository = ventaRepository;
    }

    /**
     * Obtiene todas las ventas.
     *
     * @return ResponseEntity con la lista de ventas y el código de estado 200
     * OK.
     */
    @GetMapping
    public ResponseEntity<List<VentaDTO>> getAllVentas() {
        return ResponseEntity.ok(ventaServiceImpl.findAll());
    }

    /**
     * Obtiene una venta por su ID.
     *
     * @param id ID de la venta a obtener.
     * @return ResponseEntity con la venta solicitada y el código de estado 200
     * OK.
     */
    @GetMapping("/{id}")
    public ResponseEntity<VentaDTO> getVenta(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(ventaServiceImpl.get(id));
    }

    /**
     * Crea una nueva venta.
     *
     * @param ventaDTO DTO de la venta a crear.
     * @return ResponseEntity con el ID de la venta creada y el código de estado
     * 201 Created.
     */
    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createVenta(@RequestBody @Valid final VentaDTO ventaDTO) {
        final Long createdId = ventaServiceImpl.create(ventaDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    /**
     * Actualiza una venta existente.
     *
     * @param id ID de la venta a actualizar.
     * @param ventaDTO DTO de la venta con los nuevos datos.
     * @return ResponseEntity con el ID de la venta actualizada y el código de
     * estado 200 OK.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Long> updateVenta(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final VentaDTO ventaDTO) {
        Venta venta = ventaRepository.findById(id).orElseThrow(NotFoundException::new);

        Venta ventaActualizada = ventaServiceImpl.updateVenta(ventaDTO, venta);

        ventaServiceImpl.update(id, ventaServiceImpl.mapToDTO(ventaActualizada));

        return ResponseEntity.ok(id);
    }

    /**
     * Elimina una venta existente.
     *
     * @param id ID de la venta a eliminar.
     * @return ResponseEntity con el código de estado 204 No Content.
     */
    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Void> deleteVenta(@PathVariable(name = "id") final Long id) {
        ventaServiceImpl.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Busca ventas según un atributo y un valor dados.
     *
     * @param atributo Atributo por el cual buscar.
     * @param valor Valor a buscar.
     * @param operador (Opcional) Operador de comparación (mayor, menor, igual,
     * like).
     * @return ResponseEntity con la lista de ventas que coinciden con la
     * búsqueda y el código de estado 200 OK.
     */
    @GetMapping("/buscar")
    public ResponseEntity<?> buscar(@RequestParam String atributo, @RequestParam String valor,
            @RequestParam(required = false) String operador) {

        // Validación de atributos
        boolean atributoValido = false;
        for (Field field : VentaDTO.class.getDeclaredFields()) {
            if (field.getName().equals(atributo)) {
                atributoValido = true;
                break;
            }
        }
        if (!atributoValido) {
            return ResponseEntity.badRequest().body("Atributo no válido: " + atributo);
        }

        // Conversión de tipos
        Object valorConvertido = null;
        try {
            valorConvertido = switch (VentaDTO.class.getDeclaredField(atributo).getType().getName()) {
                case "java.lang.Integer" ->
                    Integer.parseInt(valor);
                case "java.lang.Double" ->
                    Double.parseDouble(valor);
                case "java.time.LocalDate" ->
                    LocalDate.parse(valor);
                case "java.lang.Boolean" ->
                    Boolean.parseBoolean(valor);
                default ->
                    valor;
            };
        } catch (NoSuchFieldException | NumberFormatException e) {
            return ResponseEntity.badRequest().body("Error al convertir el valor: " + valor);
        }

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Venta> criteriaQuery = criteriaBuilder.createQuery(Venta.class);

        Root<Venta> root = criteriaQuery.from(Venta.class);

        Predicate predicate;

        if (operador != null) {
            switch (operador.toLowerCase()) {
                case "mayor" ->
                    predicate = criteriaBuilder.greaterThan(root.get(atributo), valorConvertido.toString());
                case "menor" ->
                    predicate = criteriaBuilder.lessThan(root.get(atributo), valorConvertido.toString());
                case "igual" ->
                    predicate = criteriaBuilder.equal(root.get(atributo), valorConvertido);
                case "like" ->
                    predicate = criteriaBuilder.like(root.get(atributo), "%" + valorConvertido + "%");
                default -> {
                    return ResponseEntity.badRequest().body("Operador no válido: " + operador);
                }
            }
        } else {
            predicate = criteriaBuilder.equal(root.get(atributo), valorConvertido);
        }

        criteriaQuery.select(root).where(predicate);

        List<Venta> resultados = entityManager.createQuery(criteriaQuery).getResultList();

        List<VentaDTO> resultadosDTO = ventaServiceImpl.mapToDTOList(resultados);

        return ResponseEntity.ok(resultadosDTO);
    }

    /**
     * Calcula las ganancias diarias para una fecha específica.
     *
     * @param fecha Fecha para la cual se calculan las ganancias diarias.
     * @return ResponseEntity con las ganancias diarias y el código de estado
     * 200 OK.
     */
    @GetMapping("/ganancias/diarias")
    public ResponseEntity<Double> calcularGananciasDiarias(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        Double gananciasDiarias = ventaServiceImpl.calcularGananciasDiarias(fecha);
        return ResponseEntity.ok(gananciasDiarias);
    }

    /**
     * Calcula las ganancias mensuales para un mes y año específicos.
     *
     * @param mes Mes para el cual se calculan las ganancias mensuales (1 para
     * enero, 2 para febrero, etc.).
     * @param anio Año para el cual se calculan las ganancias mensuales.
     * @return ResponseEntity con las ganancias mensuales y el código de estado
     * 200 OK.
     */
    @GetMapping("/ganancias/mensuales")
    public ResponseEntity<Double> calcularGananciasMensuales(@RequestParam Integer mes, @RequestParam Integer anio) {
        Double gananciasMensuales = ventaServiceImpl.calcularGananciasMensuales(mes, anio);
        return ResponseEntity.ok(gananciasMensuales);
    }

    /**
     * Calcula las ganancias dentro de un rango de fechas específico.
     *
     * @param fechaInicio Fecha de inicio del rango para el cálculo de
     * ganancias.
     * @param fechaFin Fecha de fin del rango para el cálculo de ganancias.
     * @return ResponseEntity con las ganancias dentro del rango de fechas y el
     * código de estado 200 OK.
     */
    @GetMapping("/ganancias/rango-fechas")
    public ResponseEntity<Double> calcularGananciasEnRangoFechas(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        Double gananciasEnRango = ventaServiceImpl.calcularGananciasEnRangoFechas(fechaInicio, fechaFin);
        return ResponseEntity.ok(gananciasEnRango);
    }
}
