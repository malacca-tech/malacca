package org.malacca.engine.core

import org.malacca.entry.common.holder.SoapEntryHolder
import org.malacca.entry.holder.SpringHttpEntryHolder
import org.malacca.entry.register.AbstractEntryRegister
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component

@Component
class CommonEntryRegister : AbstractEntryRegister(), InitializingBean {

    override fun afterPropertiesSet() {
        putHolder("soapEntry", SoapEntryHolder())
        putHolder("springHttpEntry", SpringHttpEntryHolder())
    }
}