//
//  EventDetailScreen.swift
//  fittableIOS
//
//  Created by Oleksandr Petrov on 19.12.2023.
//

import SwiftUI
import shared

struct EventDetailScreen: View {
    let id: String
    let colorScheme = TokenColor()
    @StateObject var viewModel: ViewModel
    
    init(id: String) {
        self.id = id
        self._viewModel = StateObject(wrappedValue: ViewModel(id: id))
    }
    
    var body: some View {
        VStack(spacing: 0){
            if let detail = viewModel.event {
                TopBar(detail: detail)
                EventDetailInternal(detail: detail)
                    .padding(.vertical, 16)
                    .padding(.horizontal, 12)
            }
            Spacer()
        }
    }
    
    
    @ViewBuilder func TopBar(detail: EventDetail_) -> some View{
        HStack(spacing: 24){
            Image("ArrowBack")
                .padding(.leading, 12)
                .foregroundColor(colorScheme.onPrimary)
            
            Text(detail.course)
                .font(.title)
                .foregroundColor(colorScheme.onPrimary)
            
            Spacer()
            
            Text("#\(detail.sequenceNumber)")
                .font(.body)
                .foregroundColor(colorScheme.onPrimary)
        }
        .frame(maxWidth: .infinity)
        .frame(height: 64)
        .onTapGesture {
            viewModel.onArrowBackClick()
        }
        .background(colorScheme.primary)
    }
    
    @ViewBuilder
    func EventDetailError(onReloadClick: @escaping () -> Void) -> some View {
        VStack(spacing: 24) {
            Image(systemName: "Error")
                .foregroundColor(colorScheme.primary)
            
            Text("An unknown error occurred.")
                .multilineTextAlignment(.center)
            
            Button(action: onReloadClick) {
                Text("Reload")
            }
        }
        .padding(24)
        .frame(maxWidth: .infinity, maxHeight: .infinity)
    }

    @ViewBuilder
    func EventDetailInternal(detail: EventDetail_) -> some View {
        VStack(spacing: 16) {
            let startFormatted = detail.starts.formatAsHoursAndMinutes()
            let endFormatted = detail.ends.formatAsHoursAndMinutes()
            EventDate(startsDate: detail.startsDate.description(), startsTime: startFormatted, endsTime: endFormatted)
            Location(room: detail.room)
            Capacity(occupied: detail.occupied.description, capacity: detail.capacity.description, parallel: detail.parallel)
        }
    }

    @ViewBuilder
    func EventDate(startsDate: String, startsTime: String, endsTime: String) -> some View {
        HStack(alignment: .center, spacing: 16) {
            Image("Calendar")
                .foregroundColor(colorScheme.primary)
            
            Text(startsDate)
                .font(.body)
            
            Spacer()
            
            Text("\(startsTime)-\(endsTime)")
                .font(.body)
                .foregroundColor(.secondary)
                .multilineTextAlignment(.trailing)
        }
    }

    @ViewBuilder
    func Location(room: String) -> some View {
        HStack(alignment: .center, spacing: 16) {
            Image("Location")
                .foregroundColor(colorScheme.primary)
            
            Text(room)
                .font(.body)
            
            Spacer()
        }
    }

    @ViewBuilder
    func Capacity(occupied: String, capacity: String, parallel: String) -> some View {
        HStack(alignment: .center, spacing: 16) {
            Image("PersonTwo")
                .foregroundColor(colorScheme.primary)
            
            VStack(alignment: .leading, spacing: 4) {
                Text(occupied)
                    .font(.body)
                
                Text("Students")
                    .font(.body)
                    .foregroundColor(.secondary)
            }
            
            Spacer()
            
            VStack(alignment: .center, spacing: 4) {
                Text(capacity)
                    .font(.body)
                
                Text("Capacity")
                    .font(.body)
                    .foregroundColor(.secondary)
            }
            
            Spacer()
            
            VStack(alignment: .trailing, spacing: 4) {
                Text("#\(parallel)")
                    .font(.body)
                
                Text("Parallel")
                    .font(.body)
                    .foregroundColor(.secondary)
            }
        }
    }
}
