//
//  AuthorizationViewModel.swift
//  fittableIOS
//
//  Created by Oleksandr Petrov on 12.12.2023.
//

import Foundation
import OAuthSwift
import SwiftUI
import shared
import Stinsen

extension AuthorizationScreen{
    @MainActor class  ViewModel : ObservableObject{
        @RouterObject var mainCoordinator : NavigationRouter<MainCoordinator>!
        
        let saveAuthorizationToken = AuthorizationDependencyProvider().saveAuthorizationTokenUseCase
        
        let oauthswift = OAuth2Swift(
            consumerKey:    "69be8dc8-2117-4735-adc0-19102e8ef456",
            consumerSecret: "", // Not used in communication
            authorizeUrl:   "https://auth.fit.cvut.cz/oauth/oauth/authorize",
            responseType:   "token"
        )
        
        func onAuthorizationClick(){
            let handle = oauthswift.authorize(
                withCallbackURL: "fit-timetable://oauth/callback",
                scope: "urn:ctu:oauth:sirius:personal:read", state:"CTU") { result in
                switch result {
                case .success(let (credential, response, parameters)):
                  print(credential.oauthToken)
                    let token = credential.oauthToken
                    Task {
                        do{
                            try await self.saveAuthorizationToken.invoke(rawData: token)
                        }catch{
                            print(error.localizedDescription)
                        }
                    }
                    if(!token.isEmpty){
                        self.mainCoordinator.coordinator.routeToTimetable()
                    }
                case .failure(let error):
                  print(error.localizedDescription)
                }
            }
            
        }
        
    }
}
