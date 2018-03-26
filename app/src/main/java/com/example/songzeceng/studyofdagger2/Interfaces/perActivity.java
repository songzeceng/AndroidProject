package com.example.songzeceng.studyofdagger2.Interfaces;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by songzeceng on 2018/3/25.
 */

@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface perActivity {}
