//
//  AuthorizationScreen.swift
//  fittableIOS
//
//  Created by Oleksandr Petrov on 10.12.2023.
//

import SwiftUI
import OAuthSwift

struct AuthorizationScreen: View {
    @StateObject var viewModel = ViewModel()
    var body: some View {
        AuthorizationScreenInternal()
    }
    
    @ViewBuilder func AuthorizationScreenInternal() -> some View {
        VStack(alignment: .center){
            Spacer().frame(height: 64)
            Image("FittableLogo")
            Spacer().frame(height: 48)
            Text("Log into your personal university time table with your CTU account")
                .padding(.horizontal, 48)
                .multilineTextAlignment(.center)
            Spacer().frame(height: 64)
            Button(action: {viewModel.onAuthorizationClick()}){
                Text("Log in")
                    .frame(maxWidth: .infinity)
                    .foregroundColor(Color.white)
                    .padding(.all, 12)
                    .background(Color.blue)
                    .clipShape(RoundedRectangle(cornerRadius: 24))
                    .padding(.horizontal, 48)
            }
            Spacer()
            Image("CvutLogoBackground").resizable().frame(width: 400, height: 390)
        }.frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .top).onOpenURL { url in
            
            let notification = Notification(name: OAuthSwift.didHandleCallbackURL, object: nil,
                                            userInfo: ["OAuthSwiftCallbackNotificationOptionsURLKey": url])
            
            NotificationCenter.default.post(notification)
        }
    }
}

struct AuthorizationScreen_Previews: PreviewProvider {
    static var previews: some View {
        AuthorizationScreen()
    }
}
