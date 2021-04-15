// IMyAidlInterface.aidl
package com.otus.services;
import com.otus.services.Circle;

interface IMyAidlInterface {

   int getMagicNumber();

   void fillCircle(inout Circle circle);
}