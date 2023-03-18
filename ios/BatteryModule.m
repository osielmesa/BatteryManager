//
//  BatteryModule.m
//  BatteryManager
//
//  Created by Osiel Lima Díaz on 18.3.23..
//

#import <Foundation/Foundation.h>

#import "React/RCTBridgeModule.h"

@interface RCT_EXTERN_MODULE(BatteryModule, NSObject) // Exposing our swift module to JS

RCT_EXTERN_METHOD(nativeLogger: (NSString)tag withDescription:(NSString)description) // Exposing our swift method to JS

@end
