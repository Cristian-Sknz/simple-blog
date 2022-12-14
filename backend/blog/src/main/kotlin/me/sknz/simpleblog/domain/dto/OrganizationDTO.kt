package me.sknz.simpleblog.domain.dto

import jakarta.validation.constraints.NotEmpty

class OrganizationDTO {

    @NotEmpty
    lateinit var name: String
    var public: Boolean? = null

}