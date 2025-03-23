//
//  consultaCepIosApp.swift
//  consultaCepIos
//
//  Created by João Guilherme Brasil Pichetti on 08/03/25.
//

import SwiftUI
import Shared

@main
struct consultaCepIosApp: App {
    var body: some Scene {
        WindowGroup {
            ContentView(viewModel: .init(cepRepository: CepRepository(), cepValidator: CepValidator()))
        }
    }
}
