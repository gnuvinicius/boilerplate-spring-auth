package br.com.garage.store.interfaces.rest;

import br.com.garage.commons.utils.userInfo.UserAuthInfo;
import br.com.garage.commons.utils.userInfo.UserInfo;
import br.com.garage.store.models.Compra;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/checkout")
public class CompraResource {

    @GetMapping
    public ResponseEntity<?> checkout(@RequestHeader(value = "x-cpf") String cpf, @UserInfo UserAuthInfo user) {

        Compra compra = Compra.builder().build();

        return ResponseEntity.ok().build();
    }
}
