//
//  fittableIOSApp.swift
//  fittableIOS
//
//  Created by Oleksandr Petrov on 21.10.2023.
//

import SwiftUI
import shared

@main
struct fittableIOSApp: App {
    init(){
        DiHelperKt.doInitKoin()
    }
    var body: some Scene {
        WindowGroup{
            MainCoordinator().view()
        }
    }
}
