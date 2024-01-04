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
    private let dayViewHeight = 80.0
    
    @State var safeAreaInsets: EdgeInsets = .init()
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
        .ignoresSafeArea(edges: .bottom)
    }
    
    @ViewBuilder func content(
        hours : Array<TimetableHour>?,
        events: Array<TimetableItem>?
    ) -> some View {
        ZStack{
            if(hours != nil){
                hoursGrid(hours: hours!)
                HStack(spacing: 0){
                    Divider()
                        .background(colorScheme.onSurfaceVariant)
                        .padding(.leading, gridStartOffset * 2)
                        .frame(width: 1)
                        .frame(maxHeight: .infinity)
                    Spacer()
                }
            }
            if(events != nil){
                eventsList(events: viewModel.events!)
            }
        }
    }
    
    @ViewBuilder func eventsList(
        events: Array<TimetableItem>
    ) -> some View {
        ScrollView(showsIndicators: false) {
            VStack(spacing: 0){
                
                Spacer()
                    .frame(height: dayViewHeight)
                
                ForEach(events.indices, id: \.self) { index in
                    let item = events[index]
                    switch item {
                    case let item as TimetableSpacer:
                        eventSpacer(spacer: item)
                            .frame(maxWidth: .infinity)
                        
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
                offset = value
            }
        }.coordinateSpace(name: "scroll")
    }
    
    @ViewBuilder func eventSpacer(
        spacer: TimetableSpacer
    ) -> some View {
        let height = spacer.convertToHeight(hourHeight: Int32(defaultHourHeight))
        Spacer()
            .frame(height: CGFloat(height))
    }
    
    @ViewBuilder func eventContainer(
        container : TimetableEventContainer
    ) -> some View {
        HStack(spacing: 0){
            ForEach(container.events.indices, id: \.self){ index in
                let event = container.events[index]
                VStack(spacing: 0){
                    if(event.spacerStart != nil){
                        eventSpacer(spacer: event.spacerStart!)
                    }
                    eventItem(event: event.event)
                        .padding(.horizontal, 4)
                    if(event.spacerEnd != nil){
                        eventSpacer(spacer: event.spacerEnd!)
                    }
                }
                .frame(maxWidth: .infinity)
            }
        }
        .padding(.leading, gridStartOffset)
        .frame(maxWidth: .infinity)
    }
    
    @ViewBuilder func eventItem(
        event : TimetableSingleEvent
    ) -> some View{
        let itemPadding = 8
        let height = event.convertToHeight(hourHeight: Int32(defaultHourHeight)) - Int32(itemPadding * 2)
        HStack(alignment: .center, spacing: 0){
            VStack(alignment: .leading, spacing: 0){
                Text(event.title)
                    .foregroundColor(colorScheme.onPrimary)
                    .font(.title3)
                
                Spacer()
                
                Text(event.room)
                    .foregroundColor(colorScheme.onPrimary)
                    .font(.subheadline)
            }
            
            Spacer()
            
            VStack(alignment: .leading){
                let startFormatted = event.start.formatAsHoursAndMinutes()
                let endFormatted = event.end.formatAsHoursAndMinutes()
                
                Text("\(startFormatted)- \n\(endFormatted)")
                    .foregroundColor(colorScheme.onPrimary)
                    .font(.subheadline)
                
                Spacer()
            }
        }
        .frame(height: CGFloat(height))
        .padding(CGFloat(itemPadding))
        .padding(.top, 8)
        .background(colorScheme.primary)
        .clipShape(RoundedRectangle(cornerRadius: 12))
        .onTapGesture {
            viewModel.onEventClick(id: event.id)
        }
    }
    
    @ViewBuilder func hoursGrid(
        hours: Array<TimetableHour>
    ) -> some View{
        VStack(spacing: 0){
            
            currentDayItem()
                .frame(maxWidth: .infinity)
            
            ScrollView(showsIndicators: false) {
                VStack(spacing: 0){
                    ForEach(hours, id: \.self){ item in
                        hourGridItem(hour: item.hour)
                            .frame(maxWidth: .infinity)
                            .padding(.leading, 8)
                    }
                }.offset(y: -offset)
            }.disabled(true)
        }
    }
    
    @ViewBuilder func currentDayItem() -> some View{
        VStack(alignment: .leading, spacing: 0) {
            HStack(spacing: 0) {
                VStack(spacing: 0) {
                    Spacer().frame(height: 4)
                    Text(viewModel.selectedDate.dayOfWeek.name.prefix(3))
                        .font(.body)
                    ZStack {
                        Circle()
                            .fill(colorScheme.secondary)
                            .frame(width: 36, height: 36)
                        Text(String(viewModel.selectedDate.dayOfMonth.description))
                            .font(.body)
                            .foregroundColor(colorScheme.onSecondary)
                    }
                }
            }
            Spacer().frame(height: 4)
            VStack(spacing: 0){
                Divider()
                    .frame(height: 1)
                    .frame(maxWidth: .infinity)
                    .background(colorScheme.onSurfaceVariant)
            }
        }
        .padding(.leading, 16)
        .frame(height: dayViewHeight)
        .frame(maxWidth: .infinity)
    }
    
    @ViewBuilder func hourGridItem(
        hour: String
    ) -> some View{
        HStack(alignment: .top, spacing: 0){
            Text("\(hour)")
                .font(.footnote)
                .foregroundColor(colorScheme.outline)
                .frame(width: 50)
            
            Spacer()
                .frame(width: 12)
            
            VStack(spacing: 0){
                Divider()
                    .frame(height: 1)
                    .frame(maxWidth: .infinity)
                    .background(colorScheme.onSurfaceVariant)
            }
        }
        .frame(height: defaultHourHeight, alignment: .top)
    }
    
    @ViewBuilder func header() -> some View{
        VStack{
            HStack{
                HStack{
                    Text("\(monthToString(month:controller.yearMonth.month)) \(String(controller.yearMonth.year))")
                        .font(.title)
                        .foregroundColor(colorScheme.onPrimary)
                    
                    Image("ArrowDown")
                        .foregroundColor(colorScheme.onPrimary)
                        .rotationEffect(.degrees(iconRotation))
                    
                    Spacer()
                }
                .frame(height: 64)
                .onTapGesture {
                    withAnimation {
                        showCalendar = !showCalendar
                        if(iconRotation == 0.0){
                            iconRotation = 180.0
                        } else {
                            iconRotation = 0.0
                        }
                    }
                    
                }
                Spacer()
                HStack{
                    if(viewModel.search != nil){
                        Image("RemoveFilter")
                            .foregroundColor(colorScheme.onPrimary)
                            .onTapGesture {
                                viewModel.removeFilter()
                            }
                    }
                    
                    Image("Search")
                        .foregroundColor(colorScheme.onPrimary)
                        .onTapGesture {
                            viewModel.onSearchClick()
                        }
                }
            }
            .padding(.horizontal, 16)
            .frame(maxWidth: .infinity)
            .frame(height: 64)
            
            if(showCalendar){
                calendar()
                    .frame(maxHeight: 300)
            }
        }
        .background(colorScheme.primary)
    }
    
    @ViewBuilder func calendar() -> some View {
        GeometryReader { reader in
            VStack(spacing: 0) {
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
                                .onTapGesture{
                                    focusDate = (date != focusDate ? date : nil)
                                    if(date.date != nil){
                                        viewModel.onDateSelected(date: date.date!)
                                    }
                                }
                        }else if(date == focusDate){
                            Circle()
                                .padding(4)
                                .frame(width: geometry.size.width, height: geometry.size.height, alignment: .center)
                                .foregroundColor(colorScheme.secondaryContainer)
                            Text("\(date.day)")
                                .frame(width: geometry.size.width, height: geometry.size.height, alignment: .center)
                                .font(.system(size: 10, weight: .bold, design: .default))
                                .foregroundColor(colorScheme.onPrimaryContainer)
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
