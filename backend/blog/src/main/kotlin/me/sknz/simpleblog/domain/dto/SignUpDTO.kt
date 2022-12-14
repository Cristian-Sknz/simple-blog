package me.sknz.simpleblog.domain.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

class SignUpDTO {

    @NotBlank(message = "Campo 'name' não pode ser vazio ou nulo")
    lateinit var name: String

    @NotBlank
    @Pattern(regexp = "^(?=[a-zA-Z0-9._]{8,20}$)(?!.*[_.]{2})[^_.].*[^_.]$",
        message = "Insira um username valido com 8-20 caracteres")
    lateinit var username: String

    @Email(message = "Preencha um email valido!")
    @NotBlank(message = "Campo 'email' não pode ser vazio ou nulo")
    lateinit var email: String

    @NotBlank(message = "Campo 'password' não pode ser vazio ou nulo")
    lateinit var password: String

    var image: String? = null
}
