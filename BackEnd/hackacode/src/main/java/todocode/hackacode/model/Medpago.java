package todocode.hackacode.model;

import lombok.Getter;

@Getter
public enum Medpago {
    EFECTIVO(0.0),
    DEBITO(0.03),
    CREDITO(0.09),
    MONEDEROVIRTUAL(0.0),
    TRANSFERENCIA(0.0245);

    private final double comision;

    Medpago(double comision) {
        this.comision = comision;
    }

}