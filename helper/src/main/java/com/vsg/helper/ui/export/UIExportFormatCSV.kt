package com.vsg.helper.ui.export

import android.app.Activity
import com.fasterxml.jackson.core.FormatSchema
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.ObjectWriter
import com.fasterxml.jackson.dataformat.csv.CsvGenerator
import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.dataformat.csv.CsvParser
import com.fasterxml.jackson.dataformat.csv.CsvSchema
import com.vsg.helper.common.export.ExportGenericEntityValue
import com.vsg.helper.common.export.ExportType
import com.vsg.helper.common.export.IEntityExport
import com.vsg.helper.common.model.IEntity
import com.vsg.helper.helper.permission.HelperPermission.Static.checkedPermissionStorage
import java.io.File
import java.io.FileWriter

class UIExportFormatCSV<TEntity> :
    IUIExportFormat<TEntity>
        where TEntity : IEntityExport,
              TEntity : IEntity {

    override fun toFile(data: TEntity, activity: Activity, path: String): File? {
        if (!data.export().hasItems) return null
        if (path.isEmpty()) return null
        return try {
            if (!activity.checkedPermissionStorage()) return null
            val fileName = data.nameFile(ExportType.CSV, true)
            val ruta: File? = activity.getExternalFilesDir(path)
            ruta?.mkdirs()
            val file = File(ruta, fileName)
            val mapper = getMapper(data.export().values.first())
            FileWriter(file).use { writer ->
                mapper.writeValues(writer)
                    .writeAll(data.export().values)
                    .close()
            }
            file
        } catch (e: Exception) {
            null
        }
    }

    override fun toDataString(data: TEntity): String? {
        try {
            val export = UIExportXML<TEntity>()
            val jNode: MutableList<JsonNode> = mutableListOf()

            jNode.add(ObjectMapper().readTree(export.toJson(data)))
            val c = mutableListOf<Pair<String, JsonNode>>()
            val f = jNode.first().fields()
            while (f.hasNext()) {
                val s = f.next()
                c.add(Pair(s.key, s.value))
            }
            val csvSchemaBuilder: CsvSchema.Builder = CsvSchema.builder()
            val destino: MutableList<String> = mutableListOf()
            c.forEach { destino.add(it.first) }

            destino.forEach { csvSchemaBuilder.addColumn(it) }
            val csvSchema: CsvSchema =
                csvSchemaBuilder.build().withColumnSeparator('\t').withLineSeparator("\n")
                    .withHeader().withoutQuoteChar();
            val result: String = CsvMapper().writer(csvSchema).writeValueAsString(jNode);
            return result;
        } catch (e: Exception) {
            throw e
        }
    }

    private fun getMapper(data: ExportGenericEntityValue): ObjectWriter {
        val csvMapper: CsvMapper = CsvMapper().enable(CsvParser.Feature.WRAP_AS_ARRAY)
            .enable(CsvGenerator.Feature.ALWAYS_QUOTE_STRINGS)
        val schema: CsvSchema =
            csvMapper.schemaFor(data::class.java).withHeader().withUseHeader(true)
                .withColumnSeparator(CsvSchema.DEFAULT_COLUMN_SEPARATOR).withLineSeparator("\n")
        return csvMapper.writer(schema as FormatSchema)
    }
}