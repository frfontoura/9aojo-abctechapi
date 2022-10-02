package br.com.fiap.abctechapi.extensions

import java.util.UUID

fun String.toUUID(): UUID = UUID.fromString(this)