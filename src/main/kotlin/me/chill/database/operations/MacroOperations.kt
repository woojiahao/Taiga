package me.chill.database.operations

import me.chill.database.Macro
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

fun getMacroList(serverId: String) =
  transaction {
    Macro
      .select { Macro.serverId eq serverId }
      .map { it[Macro.macroName] }
  }

fun getMacro(serverId: String, macroName: String) =
  transaction {
    Macro
      .select { macroMatchCondition(serverId, macroName.toLowerCase()) }
      .first()[Macro.macroDescription]
  }

fun hasMacro(serverId: String, macroName: String) =
  transaction {
    Macro
      .select { macroMatchCondition(serverId, macroName) }
      .count() > 0
  }

fun addMacro(serverId: String, macroName: String, macroDescription: String) =
  transaction {
    Macro.insert {
      it[Macro.serverId] = serverId
      it[Macro.macroName] = macroName
      it[Macro.macroDescription] = macroDescription
    }
  }

fun removeMacro(serverId: String, macroName: String) {
  transaction {
    Macro.deleteWhere { macroMatchCondition(serverId, macroName) }
  }
}

fun editMacro(serverId: String, macroName: String, macroDescription: String) {
  transaction {
    Macro.update({ macroMatchCondition(serverId, macroName) }) {
      it[Macro.macroDescription] = macroDescription
    }
  }
}

private fun macroMatchCondition(serverId: String, macroName: String) =
  (Macro.serverId eq serverId) and (Macro.macroName eq macroName)
