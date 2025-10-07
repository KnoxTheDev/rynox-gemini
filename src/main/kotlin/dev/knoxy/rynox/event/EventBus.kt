package dev.knoxy.rynox.event

import kotlinx.coroutines.runBlocking
import kotlin.reflect.KClass
import kotlin.reflect.full.superclasses

class EventBus {
  private val handlers = mutableMapOf<KClass<out Event>, MutableList<Any>>()

  inline fun <reified T : Event> register(handler: Any) {
    val eventClass = T::class
    handlers.getOrPut(eventClass) { mutableListOf() }.add(handler)
  }

  inline fun <reified T : Event> unregister(handler: Any) {
    handlers[T::class]?.remove(handler)
  }

  fun post(event: Event) {
    val eventClass = findEventClass(event::class)
    val chain = handlers[eventClass]
      ?.map { it to getMethod(it, eventClass) }
      ?.filter { it.second != null }
      ?.sortedBy { it.second!!.priority } ?: return

    runBlocking {
      chain.forEach { (handler, method) ->
        if (!event.isCancelled()) {
          method?.invoke(handler, event)
        }
      }
    }
  }

  private fun findEventClass(clazz: KClass<out Any>): KClass<out Event> {
    return clazz.superclasses.find { it.isSubclassOf(Event::class) } as? KClass<out Event>
      ?: clazz as KClass<out Event>
  }

  private fun getMethod(
    handler: Any,
    eventClass: KClass<out Event>
  ): (Event) -> Unit? {
    val method = handler::class.members.find {
      it.name == "on${eventClass.simpleName}" &&
        it.parameters.size == 2 &&
        it.parameters[1].type.classifier == eventClass
    } ?: return null
    return { event -> method.call(handler, event) }
  }

  private val KClass<*>.isSubclassOf get() = this == Event::class ||
    superclasses.any { it.isSubclassOf }
}