//
//  ContentView.swift
//  consultaCepIos
//
//  Created by João Guilherme Brasil Pichetti on 08/03/25.
//

import SwiftUI
import Shared

struct ContentView: View {
    @State private var cep: String = ""
    
    @ObservedObject var viewModel: ContentView.ViewModel
    
    var body: some View {
        VStack(alignment: .leading) {
            TextField("Digite o CEP", text: $cep)
                .textFieldStyle(RoundedBorderTextFieldStyle())
                .keyboardType(.numberPad)
                .onChange(of: cep) { oldValue, newValue in
                    let cepFormatado = viewModel.formatarCep(texto: newValue)
                    viewModel.onCepChanged(cep: cep)
                    if cepFormatado != cep {
                        cep = cepFormatado
                    }
                }
            Button(
                action: {
                    Task {
                        await viewModel.buscarCep(cep: cep)
                    }
                },
                label: {
                    ZStack {
                        Text("Buscar").opacity(viewModel.formState.isLoading ? 0 : 1)
                        if (viewModel.formState.isLoading) {
                            ProgressView()
                        }
                    }.frame(maxWidth: .infinity)
                }
            )
            .disabled(viewModel.formState.isLoading || !viewModel.formState.isDataValid)
            .buttonStyle(.bordered)
            Text("CEP: \(viewModel.formState.endereco.cep)")
                .padding(.top, 10)
            Text("Logradouro: \(viewModel.formState.endereco.logradouro)")
            Text("Bairro: \(viewModel.formState.endereco.bairro)")
            Text("Localidade: \(viewModel.formState.endereco.localidade)")
            Text("UF: \(viewModel.formState.endereco.uf)")
            Spacer()
        }
        .padding()
    }
}

extension ContentView {
    
    class CepFormState {
        let isDataValid: Bool
        let isLoading: Bool
        let hasErrorLoading: Bool
        let endereco: Endereco
        
        init(
            isDataValid: Bool = false,
            isLoading: Bool = false,
            hasErrorLoading: Bool = false,
            endereco: Endereco = Endereco(cep: "", logradouro: "", bairro: "", localidade: "", uf: "")
        ) {
            self.isDataValid = isDataValid
            self.isLoading = isLoading
            self.hasErrorLoading = hasErrorLoading
            self.endereco = endereco
        }
        
        func copy(
            isDataValid: Bool? = nil,
            isLoading: Bool? = nil,
            hasErrorLoading: Bool? = nil,
            endereco: Endereco? = nil
        ) -> CepFormState {
            return CepFormState(
                isDataValid: isDataValid ?? self.isDataValid,
                isLoading: isLoading ?? self.isLoading,
                hasErrorLoading: hasErrorLoading ?? self.hasErrorLoading,
                endereco: endereco ?? self.endereco
            )
        }
    }
    
    class ViewModel: ObservableObject {
        @Published var formState: CepFormState = CepFormState()
        
        let cepRepository: CepRepository
        let cepValidator: CepValidator
        
        init(cepRepository: CepRepository, cepValidator: CepValidator) {
            self.cepRepository = cepRepository
            self.cepValidator = cepValidator
        }
        
        func buscarCep(cep: String) async {
            if !formState.isDataValid || formState.isLoading {
                return
            }
            await MainActor.run {
                formState = formState.copy(
                    isLoading: true,
                    hasErrorLoading: false
                )
            }
            do {
                let endereco = try await cepRepository.buscarCep(cep: cep)
                await MainActor.run {
                    formState = formState.copy(
                        isLoading: false,
                        endereco: endereco
                    )
                }
            } catch {
                print("Erro ao consultar o CEP: \(error)")
                await MainActor.run {
                    formState = formState.copy(
                        isLoading: false,
                        hasErrorLoading: true
                    )
                }
            }
        }
        
        func onCepChanged(cep: String) {
            formState = formState.copy(
                isDataValid: cepValidator.verificarCep(cep: cep)
            )
        }
        
        func formatarCep(texto: String) -> String {
            let digitos = texto.filter { $0.isNumber }
            var cep = ""
            
            for (index, char) in digitos.prefix(8).enumerated() {
                if index == 5 {
                    cep.append("-")
                }
                cep.append(char)
            }
            
            return cep
        }
    }
}
