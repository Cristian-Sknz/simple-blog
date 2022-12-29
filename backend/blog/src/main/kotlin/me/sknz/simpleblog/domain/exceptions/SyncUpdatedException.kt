package me.sknz.simpleblog.domain.exceptions

import me.sknz.simpleblog.domain.model.Model
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

class SyncUpdatedException(table: String, notExists: List<Model<UUID>>): ResponseStatusException(
    HttpStatus.BAD_REQUEST,
    "Erro ao fazer a atualização dos items ${notExists.size} da tabela $table, estes itens não existem no banco de dados"
)