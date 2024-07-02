package com.sp.uxpulse.breadcrumbs

class Breadcrumb(val id: String, val screenName: String) {
    override fun toString(): String {
        return "Breadcrumb{" +
                "screenId='" + id + '\'' +
                "screenName='" + screenName + '\'' +
                '}'
    }
}
