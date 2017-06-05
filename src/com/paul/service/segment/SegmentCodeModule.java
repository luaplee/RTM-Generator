package com.paul.service.segment;

import dagger.Module;
import dagger.Provides;

@Module
public class SegmentCodeModule {
	
	@Provides
	SegmentCodeServiceImpl providesSegmentCodeService(){
		return new SegmentCodeServiceImpl();
	}

}
