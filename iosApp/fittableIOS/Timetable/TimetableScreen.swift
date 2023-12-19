//
//  TimetableScreen.swift
//  fittableIOS
//
//  Created by Oleksandr Petrov on 17.12.2023.
//

import SwiftUI
import shared
import SwiftUICalendar

struct TimetableScreen: View {
    private let defaultHourHeight = 64.0
    private let gridStartOffset = 80.0
    
    @ObservedObject var controller: CalendarController = CalendarController()
    @StateObject var viewModel = ViewModel()
    @State var focusDate: YearMonthDay? = YearMonthDay.current
    @State var showCalendar = false
    @State var iconRotation = 0.0
    @State private var offset = CGFloat.zero
    let colorScheme = TokenColor()
    
    var body: some View {
        VStack(spacing: 0){
            header()
            content(
                hours: viewModel.hours,
                events: viewModel.events
            )
            Spacer()
        }
    }
    
    @ViewBuilder func content(
        hours : Array<TimetableHour>?,
        events: Array<TimetableItem>?
    ) -> some View {
        ZStack{
            if(hours != nil){
                hoursGrid(hours: hours!).padding(.top, 8)
                HStack(spacing: 0){
                    Divider()
                        .padding(.leading, gridStartOffset * 2)
                        .frame(width: 1)
                        .frame(maxHeight: .infinity)
                    Spacer()
                }
            }
            if(events != nil){
                eventsList(events: viewModel.events!).padding(.top, 8)
            }
        }
    }
    
    @ViewBuilder func eventsList(
        events: Array<TimetableItem>
    ) -> some View {
        ScrollView{
            VStack(spacing: 0){
                ForEach(events.indices, id: \.self) { index in
                    let item = events[index]
                    switch item {
                    case let item as TimetableSpacer:
                        eventSpacer(spacer: item)
                        
                    case let item as TimetableEventContainer:
                        eventContainer(container: item)
                        
                    default:
                        EmptyView() // no-op
                    }
                }
            }.background( GeometryReader {
                Color.clear.preference(
                    key: ViewOffsetKey.self,
                    value: -$0.frame(in: .named("scroll")).origin.y
                )
            })
            .onPreferenceChange(ViewOffsetKey.self) { value in
                print("offset >> \(value)")
                offset = value
            }
        }.coordinateSpace(name: "scroll")
    }
    
    @ViewBuilder func eventSpacer(
        spacer: TimetableSpacer
    ) -> some View {
        let height = spacer.convertToHeight(hourHeight: Int32(defaultHourHeight))
        Spacer().frame(height: CGFloat(height))
    }
    
    @ViewBuilder func eventContainer(
        container : TimetableEventContainer
    ) -> some View {
        let height = container.convertToHeight(hourHeight: Int32(defaultHourHeight))
        HStack{
            ForEach(container.events.indices, id: \.self){ index in
                let event = container.events[index]
                VStack(spacing: 0){
                    if(event.spacerStart != nil){
                        eventSpacer(spacer: event.spacerStart!)
                    }
                    eventItem(event: event.event).padding(.horizontal, 4)
                    if(event.spacerEnd != nil){
                        eventSpacer(spacer: event.spacerEnd!)
                    }
                }.padding(.leading, gridStartOffset)
            }
        }
    }
    
    @ViewBuilder func eventItem(
        event : TimetableSingleEvent
    ) -> some View{
        let itemPadding = 8
        let height = event.convertToHeight(hourHeight: Int32(defaultHourHeight)) - Int32(itemPadding * 2)
        HStack(alignment: .center){
            VStack(alignment: .leading){
                Text(event.title)
                    .foregroundColor(colorScheme.onPrimary)
                    .font(.title2)
                Spacer().frame(maxWidth: .infinity)
                Text(event.room)
                    .foregroundColor(colorScheme.onPrimary)
                    .font(.subheadline)
            }
            Spacer().frame(maxWidth: .infinity)
            VStack(alignment: .leading){
                let startFormatted = event.start.formatAsHoursAndMinutes()
                let endFormatted = event.end.formatAsHoursAndMinutes()
                Text("\(startFormatted)- \n\(endFormatted)")
                    .foregroundColor(colorScheme.onPrimary)
                    .font(.subheadline)
                Spacer().frame(maxHeight: .infinity)
            }
        }
        .frame(maxWidth: .infinity)
        .frame(height: CGFloat(height))
        .padding(CGFloat(itemPadding))
        .background(colorScheme.primary)
        .clipShape(RoundedRectangle(cornerRadius: 12))
    }
    
    @ViewBuilder func hoursGrid(
        hours: Array<TimetableHour>
    ) -> some View{
        ScrollView{
            VStack(spacing: 0){
                ForEach(hours, id: \.self){ item in
                    hourGridItem(hour: item.hour)
                        .frame(maxWidth: .infinity)
                        .padding(.leading, 8)
                }
            }.offset(y: -offset)
        }.disabled(true)
    }
    
    @ViewBuilder func hourGridItem(
        hour: String
    ) -> some View{
        HStack(alignment: .top){
            Text("\(hour)")
                .font(.footnote)
                .foregroundColor(colorScheme.outline)
                .frame(width: 50)
            Spacer().frame(width: 12)
            VStack{
                Divider()
                    .frame(height: 1)
                    .frame(maxWidth: .infinity)
            }
        }.frame(height: defaultHourHeight, alignment: .top)
    }
    
    @ViewBuilder func header() -> some View{
        VStack{
            HStack{
                Text("\(monthToString(month:controller.yearMonth.month)) \(String(controller.yearMonth.year))")
                    .font(.title)
                    .foregroundColor(colorScheme.onPrimary)
                    .padding(.leading, 12)
                Spacer().frame(width: 8)
                Image("ArrowDown")
                    .foregroundColor(colorScheme.onPrimary)
                    .rotationEffect(.degrees(iconRotation))
                Spacer()
            }
            .frame(maxWidth: .infinity)
            .frame(height: 64)
            .onTapGesture {
                showCalendar = !showCalendar
                if(iconRotation == 0.0){
                    iconRotation = 180.0
                }else{
                    iconRotation = 0.0
                }
            }
            
            if(showCalendar){
                calendar().frame(maxHeight: 300)
            }
            
        }
        .background(colorScheme.primary)
    }
    
    @ViewBuilder func calendar() -> some View {
        GeometryReader { reader in
            VStack {
                CalendarView(controller, startWithMonday: true, headerSize: .fixHeight(40.0)) { week in
                    Text("\(week.shortString)")
                        .font(.headline)
                        .frame(width: reader.size.width / 7)
                        .foregroundColor(colorScheme.onPrimary)
                } component: { date in
                    GeometryReader { geometry in
                        if date.isToday {
                            Circle()
                                .padding(4)
                                .frame(width: geometry.size.width, height: geometry.size.height, alignment: .center)
                                .foregroundColor(colorScheme.secondary)
                            Text("\(date.day)")
                                .frame(width: geometry.size.width, height: geometry.size.height, alignment: .center)
                                .font(.system(size: 10, weight: .bold, design: .default))
                                .foregroundColor(colorScheme.onPrimary)
                        }else if(date == focusDate){
                            Circle()
                                .padding(4)
                                .frame(width: geometry.size.width, height: geometry.size.height, alignment: .center)
                                .foregroundColor(colorScheme.secondaryContainer)
                            Text("\(date.day)")
                                .frame(width: geometry.size.width, height: geometry.size.height, alignment: .center)
                                .font(.system(size: 10, weight: .bold, design: .default))
                                .foregroundColor(colorScheme.onTertiary)
                        } else {
                            Text("\(date.day)")
                                .frame(width: geometry.size.width, height: geometry.size.height, alignment: .center)
                                .font(.system(size: 10, weight: .light, design: .default))
                                .foregroundColor(colorScheme.onSecondary)
                                .opacity(date.isFocusYearMonth == true ? 1 : 0.4)
                                .onTapGesture{
                                    focusDate = (date != focusDate ? date : nil)
                                    if(date.date != nil){
                                        viewModel.onDateSelected(date: date.date!)
                                    }
                                }
                        }
                    }
                }
            }
        }
    }
    private func monthToString(month: Int) -> String{
        let formatter = DateFormatter()
        formatter.dateFormat = "LLLL" // "LLLL" gives the full month name
        
        // Create a Date with the given month number and a day of 1
        let date = Calendar.current.date(from: DateComponents(year: 2000, month: month, day: 1))!
        return formatter.string(from: date)
    }
}

struct ViewOffsetKey: PreferenceKey {
    typealias Value = CGFloat
    static var defaultValue = CGFloat.zero
    static func reduce(value: inout Value, nextValue: () -> Value) {
        value += nextValue()
    }
}
