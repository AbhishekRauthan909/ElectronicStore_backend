package com.electronic.store.ElectronicStore.dtos;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderRequest
{
    @NotBlank(message = "cart id is required")
    private String cartId;
    @NotBlank(message = "user id is required")
    private String userId;
    private String orderStatus="PENDING";
    private String paymentStatus="NOTPAID";
    @NotBlank(message = "Name required")
    private String billingName;
    @NotBlank(message = "Address required")
    private String billingAddress;
    @NotBlank(message = "phone no required")
    private String billingPhone;
}
