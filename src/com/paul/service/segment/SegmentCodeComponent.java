package com.paul.service.segment;

import dagger.Component;

//@Component(modules = SegmentCodeModule.class)
public interface SegmentCodeComponent {
	void inject(SegmentCodeServiceImpl obj);
}
