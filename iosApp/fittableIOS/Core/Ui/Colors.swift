//
//  Colors.swift
//  fittableIOS
//
//  Created by Oleksandr Petrov on 10.12.2023.
//

import Foundation
import SwiftUI

struct BaseColor {
    let primary = Color("Primary")
    let primaryBackground = Color("PrimaryBackground")
    let outline = Color("Outline")
    let secondary = Color("Secondary")
    let secondaryContainer = Color("SecondaryContainer")
    let onPrimary = Color("OnPrimary")
    let onSecondary = Color("OnSecondary")
    let onTertiary = Color("OnTertiary")
    let surfaceTint = Color("SurfaceTint")
    
    let whiteish = Color("Whiteish")
    let grey = Color("Grey")
}

struct TokenColor {
    let baseColor = BaseColor()
    
    let primary: Color
    let secondary: Color
    let outline: Color
    let buttons: Buttons
    let surface: Color
    let onPrimary: Color
    let onSecondary: Color
    let onTertiary: Color
    let secondaryContainer: Color
    let surfaceTint: Color
    
    struct Buttons {
        let primary: Button
    }
    
    struct Button {
        let background: Color!
        let content: Color!
    }
    
    init() {
        primary = baseColor.primary
        secondary = baseColor.secondary
        outline = baseColor.outline
        buttons = Buttons(
            primary: Button(background: baseColor.primary, content: baseColor.onPrimary)
        )
        surface = baseColor.grey
        onPrimary = baseColor.onPrimary
        onSecondary = baseColor.onSecondary
        onTertiary = baseColor.onTertiary
        secondaryContainer = baseColor.secondaryContainer
        surfaceTint = baseColor.surfaceTint
    }
}
