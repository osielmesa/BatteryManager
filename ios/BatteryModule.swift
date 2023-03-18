//
//  BatteryModule.swift
//  BatteryManager
//
//  Created by Osiel Lima Díaz on 18.3.23..
//

import Foundation

@objc(BatteryModule)
class BatteryModule: NSObject {
  
  @objc
  func nativeLogger(_ tag: NSString, withDescription description: NSString) {
    print("\(tag), \(description)");
  }
  
}
