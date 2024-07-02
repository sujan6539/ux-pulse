package com.sp.uxpulse.breadcrumbs


class BreadcrumbManager private constructor() {
    private val breadcrumbs: MutableList<Breadcrumb> = ArrayList()

    fun addBreadcrumb(breadcrumb: Breadcrumb) {
        breadcrumbs.add(breadcrumb)
    }

    fun removeBreadcrumb(id: String?) {
        for (i in breadcrumbs.indices.reversed()) {
            if (breadcrumbs[i].id == id) {
                breadcrumbs.removeAt(i)
                break
            }
        }
    }

    fun getBreadcrumbs(): List<Breadcrumb> {
        return breadcrumbs
    }

    fun clearBreadcrumbs() {
        breadcrumbs.clear()
    }

    companion object {
        @get:Synchronized
        var instance: BreadcrumbManager? = null
            get() {
                if (field == null) {
                    field = BreadcrumbManager()
                }
                return field
            }
            private set
    }
}
