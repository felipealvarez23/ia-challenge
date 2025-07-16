package co.com.bancolombia.api.mapper;

import co.com.bancolombia.api.model.processcustomerstats.request.CustomerStatsRequest;
import co.com.bancolombia.model.customerstats.CustomerStats;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerStatsMapper {
    @Mapping(source = "totalContactoClientes", target = "totalCustomerContacts")
    @Mapping(source = "motivoReclamo", target = "reasonClaim")
    @Mapping(source = "motivoGarantia", target = "reasonWarranty")
    @Mapping(source = "motivoDuda", target = "reasonQuery")
    @Mapping(source = "motivoCompra", target = "reasonPurchase")
    @Mapping(source = "motivoFelicitaciones", target = "reasonCongratulations")
    @Mapping(source = "motivoCambio", target = "reasonChange")
    CustomerStats toDomain(CustomerStatsRequest customerStatsRequest);
}
