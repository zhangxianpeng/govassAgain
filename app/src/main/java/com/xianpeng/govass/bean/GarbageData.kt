package com.xianpeng.govass.bean

data class GarbageData(
    var code: Int = -1,
    var msg: String = "",
    var newslist: List<DetailInfo> = listOf()
) {
    data class DetailInfo(
        var aipre: Int = 0,
        var contain: String = "",
        var explain: String = "",
        var name: String = "",
        var tip: String = "",
        var type: Int = 0       // 0 - 可回收垃圾 1 - 有害垃圾 2 - 厨余垃圾 3 - 其他垃圾
    )
}