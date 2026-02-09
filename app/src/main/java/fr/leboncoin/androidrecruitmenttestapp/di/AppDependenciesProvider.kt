package fr.leboncoin.androidrecruitmenttestapp.di

import fr.leboncoin.androidrecruitmenttestapp.utils.AnalyticsHelper

interface AppDependenciesProvider {
    val dependencies: AppDependencies
}

class AppDependencies {
    //val logger: Logger by lazy { Logger.getGlobal() }
    val analyticsHelper: AnalyticsHelper by lazy { AnalyticsHelper() }
    //val dataDependencies: DataDependencies by lazy { DataDependencies() }
}