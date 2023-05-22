package co.com.icesi.TallerJPA.dto;

import co.com.icesi.TallerJPA.Enum.Action;
import lombok.Builder;
import lombok.Data;


//TODO: AGREGAR ESTE RETURN A LAS ACCIONES DEL SERVICIO DE LA CUENTAS
@Data
@Builder
public class ActionResultDTO {
    private Action type;
    private String actionPerformed;
}
