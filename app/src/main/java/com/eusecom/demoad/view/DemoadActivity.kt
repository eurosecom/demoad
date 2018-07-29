package com.eusecom.demoad.view

import android.Manifest
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.arch.lifecycle.Observer
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import co.zsmb.materialdrawerkt.builders.drawer
import co.zsmb.materialdrawerkt.draweritems.badgeable.primaryItem
import co.zsmb.materialdrawerkt.draweritems.divider
import co.zsmb.materialdrawerkt.draweritems.sectionHeader
import com.eusecom.demoad.R
import com.eusecom.demoad.datamodel.model.RealmLocation
import com.eusecom.demoad.util.locationtrack.CurrentLocationListener
import com.eusecom.demoad.viewmodel.IDemoadViewModel
import com.mikepenz.materialdrawer.Drawer
import dagger.android.AndroidInjection
import org.jetbrains.anko.toast
import rx.Observable
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.os.Build
import kotlinx.android.synthetic.main.activity_demoad.*


class DemoadActivity : AppCompatActivity() {

    private val PERMISSION_REQUEST_CODE = 1000

    @Inject
    lateinit var mViewModel: IDemoadViewModel
    @Inject
    lateinit var mCurrentLocationListener: CurrentLocationListener

    private lateinit var result: Drawer
    private var textView: TextView? = null
    private var builder: StringBuilder? = null

    var mSubscription: CompositeSubscription = CompositeSubscription()

    override fun onCreate(savedInstanceState: Bundle?) {

        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demoad)
        setSupportActionBar(toolbar)

        initView()
        setSubscription()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkPermission()){
                getLocationUpdates()
            }else{
                requestPermission()
            }
        }else{
            getLocationUpdates()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        mViewModel.clearObservableSaveLocationToRealm()
        mViewModel.clearObservableClearAllLocsFromRealm()
        mSubscription?.unsubscribe()
        mSubscription?.clear()

    }

    private fun setSubscription() {

        mSubscription.add(savedLocationToRealm()
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError
                {
                    throwable ->
                    Log.e("DemoadActivity", "Error Throwable " + throwable.message)
                    toast("Server not connected")
                }
                .onErrorResumeNext(
                        { throwable -> Observable.empty() })
                .subscribe(
                        { it -> setSavedLocation(it) }))

        mSubscription.add(clearedAllLocsFromRealm()
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError
                {
                    throwable ->
                    Log.e("DemoadActivity", "Error Throwable " + throwable.message)
                    toast("Server not connected")
                }
                .onErrorResumeNext(
                        { throwable -> Observable.empty() })
                .subscribe(
                        { it -> setClearedLocations(it) }))

    }

    private fun setClearedLocations(location: List<RealmLocation>) {
        Toast.makeText(this@DemoadActivity, getString(R.string.locscleared), Toast.LENGTH_SHORT).show()
    }

    private fun setSavedLocation(location: RealmLocation) {

        Toast.makeText(this@DemoadActivity, getString(R.string.locchanged), Toast.LENGTH_SHORT).show()
        builder?.append(location.latitude)?.append(" : ")?.append(location.longitude)?.append("\n")
        textView?.setText(builder.toString())
    }

    fun savedLocationToRealm(): Observable<RealmLocation>  {
        return mViewModel.savedLocationToRealm();
    }

    fun clearTrackFromRealm(podm: String) {
        return mViewModel.emitClearAllLocsFromRealm(podm);
    }

    fun clearedAllLocsFromRealm(): Observable<List<RealmLocation>>  {
        return mViewModel.clearedLocationsFromRealm();
    }


    private fun initView() {

        textView = findViewById<TextView>(R.id.textView)
        builder = StringBuilder()

        result = drawer {

            toolbar = this@DemoadActivity.toolbar
            hasStableIds = true
            showOnFirstLaunch = false

            sectionHeader(getString(R.string.app_desc)) {
                divider = false
            }


            divider {}
            primaryItem(getString(R.string.action_mainfragment)) {

                onClick { _ ->
                    navigateToMainFragment()
                    false
                }

            }

            divider {}
            primaryItem(getString(R.string.action_cleartrack)) {

                onClick { _ ->
                    navigateToClearTrack()
                    false
                }

            }

            divider {}
            primaryItem(getString(R.string.action_settings)) {

                onClick { _ ->

                    false
                }

            }


            divider {}


        }
    }

    private fun navigateToMainFragment() {

        val `is` = Intent(this, MapsActivity::class.java)
        startActivity(`is`)
    }


    private fun navigateToClearTrack() {

        clearTrackFromRealm("1")
    }


    private fun getLocationUpdates() {
        mCurrentLocationListener.observe(this, Observer<Location> { location ->
            if (location != null) {

                mViewModel.emitSaveLocationToRealm(location)
            }
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                getLocationUpdates()
            }
        }
    }

    private fun checkPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(applicationContext, ACCESS_FINE_LOCATION)
        val result1 = ContextCompat.checkSelfPermission(applicationContext, ACCESS_COARSE_LOCATION)

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {

        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_CODE)

    }



}
