//
//  KotlinDatetimeConverter.swift
//  fittableIOS
//
//  Created by Oleksandr Petrov on 18.12.2023.
//

import Foundation
import shared

func convertToSwiftDate(kotlinDate: Kotlinx_datetimeLocalDate) -> Date{
    let rawDate = kotlinDate.description()
    let dateFormatter = DateFormatter()
    dateFormatter.locale = Locale(identifier: "en_US_POSIX")
    dateFormatter.dateFormat = "yyyy-MM-dd"
    if let date = dateFormatter.date(from: rawDate) {
        print(date)
        return date
    } else {
        print("Error converting string to date")
        return Date()
    }
}

func convertToKotlinDateTime(swiftDate : Date) -> Kotlinx_datetimeLocalDate{
    let year = Calendar.current.component(.year, from: swiftDate)
    let month = toKotlinxMonth(month: Calendar.current.component(.month, from: swiftDate))
    let day = Calendar.current.component(.day, from: swiftDate)
    
    let result = Kotlinx_datetimeLocalDate.init(year: Int32(year), month: month ?? Kotlinx_datetimeMonth.january, dayOfMonth: Int32(day))
    print(result)
    return result
}

func toKotlinxMonth(month: Int) -> Kotlinx_datetimeMonth?{
    switch month {
    case 1: return Kotlinx_datetimeMonth.january
    case 2: return Kotlinx_datetimeMonth.february
    case 3: return Kotlinx_datetimeMonth.march
    case 4: return Kotlinx_datetimeMonth.april
    case 5: return Kotlinx_datetimeMonth.may
    case 6: return Kotlinx_datetimeMonth.june
    case 7: return Kotlinx_datetimeMonth.july
    case 8: return Kotlinx_datetimeMonth.august
    case 9: return Kotlinx_datetimeMonth.september
    case 10: return Kotlinx_datetimeMonth.october
    case 11: return Kotlinx_datetimeMonth.november
    case 12: return Kotlinx_datetimeMonth.december
    default:
        print("internal error in datetime mapping")
        return nil
    }
}
