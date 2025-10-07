package dev.knoxy.rynox.event

import kotlinx.coroutines.runBlocking
import kotlin.reflect.KClass
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.full.superclasses

class EventBus {
  private val handlers = mutableListOf<Any>()

  fun register(handler: Any) {
    handlers.add(handler)
  }

  fun unregister(handler: Any) {
    handlers.remove(handler)
  }

  fun post(event: Event) {
    runBlocking {
      handlers.forEach { handler ->
        handler::class.memberFunctions
          .find { func -> func.name == "onEvent" && func.parameters.size == 2 }
          ?.call(handler, event)
      }
    }
  }

  private fun KClass<*>.isSubclassOf(): Boolean = this == Event::class ||
    superclasses.any { it.isSubclassOf() }
}