package com.example.amanuel.pockemon

import android.location.Location

/**
 * Created by amanuel on 12/23/17.
 */
class Pokemon{
    var name:String?=null
    var des:String?=null
    var image:Int?=null
    var power:Double?=null
    var location:Location?=null
    var IsCatch:Boolean?=false
    constructor(image:Int,name:String,des:String,power:Double,lat:Double,longt:Double){
        this.name=name
        this.des=des
        this.image=image
        this.power=power
        this.location= Location(name)
        this.location!!.latitude=lat
        this.location!!.longitude=longt
        this.IsCatch=false
    }


}