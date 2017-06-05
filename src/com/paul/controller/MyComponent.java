package com.paul.controller;

import dagger.Component;

@Component(modules = {SegmentControllModule.class})
public interface MyComponent {
	void inject(MainController main);

}
