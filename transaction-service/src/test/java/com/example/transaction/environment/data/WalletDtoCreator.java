package com.example.transaction.environment.data;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import com.example.transaction.dto.WalletDto;
import com.example.transaction.dto.WalletPageDto;
import com.example.transaction.dto.WalletStatusDto;
import com.example.transaction.dto.WalletWriteDto;

import static com.example.transaction.spec.integration.LifecycleSpecification.USER_ID;

@Component
@RequiredArgsConstructor
public class WalletDtoCreator {

    public WalletWriteDto createEurWalletWriteDto() {
        return createWalletWithType("00000000-1111-2222-3333-000000000005");
    }

    public WalletWriteDto createUsdWalletWriteDto() {
        return createWalletWithType("00000000-1111-2222-3333-000000000001");
    }

    private static WalletWriteDto createWalletWithType(String typeId) {
        return new WalletWriteDto()
            .name("My_name")
            .userId(USER_ID)
            .walletTypeId(typeId);
    }

    public WalletPageDto createWalletPageDto(String id) {
        return new WalletPageDto()
            .items(List.of(createWalletDto(id)));
    }

    public WalletDto createWalletDto(String id) {
        return new WalletDto()
            .id(id)
            .name("My_name")
            .walletType("Main Wallet")
            .currency("USD")
            .userId(USER_ID)
            .status(WalletStatusDto.ACTIVE)
            .balance(150.0);
    }

}
