package com.example.simsapp.ui.main

import android.R
import android.R.attr
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.telephony.SubscriptionManager
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import timber.log.Timber
import android.telephony.SubscriptionInfo
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.concurrent.Flow
import java.util.jar.Manifest
import android.content.IntentFilter

import android.telephony.euicc.EuiccManager

import android.content.Intent

import android.content.BroadcastReceiver
import android.app.PendingIntent

import android.R.attr.action
import android.telephony.CellInfo

import androidx.core.content.ContextCompat.getSystemService

import android.telephony.TelephonyManager
import androidx.core.content.ContextCompat


class MainViewModel(application: Application) : AndroidViewModel(application) {


    val currentSubscription = MutableLiveData<String> ()

    val currentSubscriptions = MutableLiveData<List<SubscriptionInfo>>()

    val cellInfos = MutableLiveData<List<CellInfo>>()

    fun listSimcards(){
        var context = getApplication<Application>().applicationContext
        val subscriptionManager : SubscriptionManager  = context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager

        if(context.checkSelfPermission(android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED){
            Timber.d("The permission has been granted")
            val activeSubscriptions = subscriptionManager.activeSubscriptionInfoList
            currentSubscriptions.value = activeSubscriptions
            for(subscription in activeSubscriptions){
                Timber.d(subscription.toString())
            }


        }else{
            //We need to request the permission to the user
            Timber.d("The permission has not been granted")
        }
    }

    fun getCurrentSubscriptionInfo(){
        var context = getApplication<Application>().applicationContext
        if(context.checkSelfPermission(android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED){
            val defaultId = SubscriptionManager.getDefaultDataSubscriptionId()
            for(subscription in currentSubscriptions.value!!){
                if(subscription.subscriptionId == defaultId) {
                    currentSubscription.value = subscription.toString()
                    Timber.d("Found default subscription : $subscription")
                }
            }
        }
    }

    fun switchToSubscription(){
        var context = getApplication<Application>().applicationContext
        val action = "switch_to_subscription"
        val subscriptionManager : SubscriptionManager  = context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
        val defaultId = SubscriptionManager.getDefaultDataSubscriptionId()
        val defaultSubscription : SubscriptionInfo
        var alterSubscription : SubscriptionInfo
        if(currentSubscriptions.value?.get(0)?.subscriptionId == defaultId){
            defaultSubscription =  currentSubscriptions.value?.get(0) as SubscriptionInfo
            alterSubscription = currentSubscriptions.value?.get(1) as SubscriptionInfo
        }else {
            defaultSubscription = currentSubscriptions.value?.get(1) as SubscriptionInfo
            alterSubscription = currentSubscriptions.value?.get(0) as SubscriptionInfo
        }

        subscriptionManager.setOpportunistic(true,defaultSubscription.subscriptionId)
        subscriptionManager.setOpportunistic(false, alterSubscription.subscriptionId)




        for(subscription in currentSubscriptions.value!!){
            //If it is not the default subcription, we have found the other one
            if (subscription.subscriptionId != defaultId){
                Timber.d("Found another subscription")
                val intent = Intent(action)
                val callbackIntent = PendingIntent.getBroadcast(
                    context, 0 /* requestCode */, intent, PendingIntent.FLAG_CANCEL_CURRENT
                )
                // Dit doet niet wat het zou moeten doen, namelijk de default subscription
                // vervangen
                //subscriptionManager.switchToSubscription(subscription.subscriptionId, callbackIntent)
                //Timber.d("Switching $defaultId for ${subscription.subscriptionId}")



            }
        }
    }


    fun listInfo(){
        var context = getApplication<Application>().applicationContext
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager?
        if(context.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            val allCellInfo = telephonyManager!!.allCellInfo
            cellInfos.value = allCellInfo
            Timber.d(allCellInfo.toString())
        }
    }



}