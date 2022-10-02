package br.com.fiap.abctechapi.config

import br.com.fiap.abctechapi.extensions.toUUID
import java.util.UUID
import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter(autoApply = true)
class UuidConverter: AttributeConverter<UUID, String> {
    override fun convertToDatabaseColumn(attribute: UUID?): String? {
        return attribute?.toString()
    }

    override fun convertToEntityAttribute(dbData: String?): UUID? {
        return dbData?.toUUID()
    }
}