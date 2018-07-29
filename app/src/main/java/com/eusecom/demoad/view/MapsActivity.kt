package com.eusecom.demoad.view

import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import co.zsmb.materialdrawerkt.builders.drawer
import co.zsmb.materialdrawerkt.draweritems.badgeable.primaryItem
import co.zsmb.materialdrawerkt.draweritems.divider
import com.eusecom.demoad.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.mikepenz.materialdrawer.Drawer
import kotlinx.android.synthetic.main.activity_maps.*
import android.widget.TextView
import com.eusecom.demoad.datamodel.model.RealmLocation
import com.eusecom.demoad.viewmodel.IDemoadViewModel
import com.google.android.gms.maps.model.*
import dagger.android.AndroidInjection
import org.jetbrains.anko.toast
import rx.Observable
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject
import com.google.android.gms.maps.model.PolylineOptions;

class MapsActivity : AppCompatActivity(), OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener {

    @Inject
    lateinit var mViewModel: IDemoadViewModel

    private lateinit var map: GoogleMap
    private lateinit var result: Drawer
    private var text2: TextView? = null

    var mSubscription: CompositeSubscription = CompositeSubscription()

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    private lateinit var sheetBehavior: BottomSheetBehavior<LinearLayout>

    var linecolor: Int = Color.BLUE
    var linesize: Float = 5.0f

    override fun onCreate(savedInstanceState: Bundle?) {

        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        initView()

        setSubscription()

    }

    private fun initView() {

        sheetBehavior = BottomSheetBehavior.from<LinearLayout>(bottom_sheet)

        sheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                    }
                    BottomSheetBehavior.STATE_EXPANDED ->
                        text1.text = getString(R.string.closesheet)
                    BottomSheetBehavior.STATE_COLLAPSED ->
                        text1.text = getString(R.string.expandsheet)
                    BottomSheetBehavior.STATE_DRAGGING -> {
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })

        text2 = findViewById<View>(R.id.text2) as TextView

        result = drawer {

            toolbar = this@MapsActivity.toolbar
            hasStableIds = true
            showOnFirstLaunch = false

            divider {}
            primaryItem(getString(R.string.action_north)) {

                onClick { _ ->
                    emitMarginLocs("N")
                    false
                }

            }
            divider {}
            primaryItem(getString(R.string.action_south)) {

                onClick { _ ->
                    emitMarginLocs("S")
                    false
                }

            }
            divider {}
            primaryItem(getString(R.string.action_west)) {

                onClick { _ ->
                    emitMarginLocs("W")
                    false
                }

            }
            divider {}
            primaryItem(getString(R.string.action_east)) {

                onClick { _ ->
                    emitMarginLocs("E")
                    false
                }

            }
            divider {}


        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        map.uiSettings.isZoomControlsEnabled = true
        map.setOnMarkerClickListener(this)

        setUpMap()
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        text2?.text = marker?.getTitle()
        expandCloseSheet()
        return true
    }

    private fun expandCloseSheet() {
        if (sheetBehavior!!.state != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED
            text1.text = getString(R.string.closesheet)
        } else {
            sheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
            text1.text = getString(R.string.expandsheet)
            text2?.text = ""
        }
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }

        map.isMyLocationEnabled = true
        map.mapType = GoogleMap.MAP_TYPE_NORMAL


    }

    private fun placeMarkerOnMap(loc: RealmLocation) {

        val curLatLng = LatLng(loc.latitude, loc.longitude)
        val markerOptions = MarkerOptions().position(curLatLng)

        val titlex = "Lati " + loc.latitude + ", Long " + loc.longitude + ", Acc " + loc.accuracy + ", Time " + loc.time + ", Prov " + loc.provider
        markerOptions.title(titlex)

        map.addMarker(markerOptions)
    }

    private fun setSubscription() {

        mSubscription.add(getMyLocsFromRealm()
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
                        { it -> setSavedLocations(it) }))

        mSubscription.add(getMarginLocs()
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
                        { it -> setMarginLocations(it) }))

    }

    private fun setSavedLocations(locations: List<RealmLocation>) {

        var listLatLng: ArrayList<LatLng> = ArrayList<LatLng>()
        var currentLatLng = LatLng(0.0, 0.0)
        locations.forEach { i ->

            currentLatLng = LatLng(i.latitude, i.longitude)
            listLatLng.add(currentLatLng)

            placeMarkerOnMap(i)

        }

        if( listLatLng.size > 0 ) {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))

            listLatLng.forEachIndexed { index, element ->

                if( index > 0) {

                    map.addPolyline(PolylineOptions()
                        .add(listLatLng.get(index), listLatLng.get(index-1))
                        .width(linesize).color(linecolor).geodesic(true));

                }

            }


        }

    }

    private fun setMarginLocations(locations: List<RealmLocation>) {

        var listLatLng: ArrayList<LatLng> = ArrayList<LatLng>()
        var currentLatLng = LatLng(0.0, 0.0)
        locations.forEach { i ->

            currentLatLng = LatLng(i.latitude, i.longitude)
            listLatLng.add(currentLatLng)

        }

        if( listLatLng.size > 0 ) {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
        }

    }


    fun getMyLocsFromRealm(): Observable<List<RealmLocation>> {
        return mViewModel.getMyLocsFromRealm();
    }

    fun getMarginLocs(): Observable<List<RealmLocation>> {
        return mViewModel.getMarginLocsFromRealm();
    }

    fun emitMarginLocs(wside: String) {
        return mViewModel.emitMarginLocsFromRealm(wside);
    }

    override fun onDestroy() {
        super.onDestroy()

        mViewModel.clearObservableMarginLocsFromRealm()
        mSubscription?.unsubscribe()
        mSubscription?.clear()

        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }

    }


}
