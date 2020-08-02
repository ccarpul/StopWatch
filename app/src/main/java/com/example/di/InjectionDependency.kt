package com.example.di

import com.example.stopwatch.HomeViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val DependenciesModuleHome = module {
    viewModel { HomeViewModel() }
}
