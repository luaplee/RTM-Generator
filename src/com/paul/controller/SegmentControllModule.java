package com.paul.controller;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class SegmentControllModule {

	@Provides
	@Singleton
	public SegmentController provideSegmentController(){
		return new SegmentController();
	}
}
