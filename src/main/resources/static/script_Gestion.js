function registrarVehiculo() {
    // Obtener el tipo de vehículo seleccionado
    const tipoVehiculo = document.getElementById("tipo_vehiculo").value;

    // Aquí puedes implementar la lógica para asignar automáticamente el piso según el tipo de vehículo

    // Ejemplo de asignación de piso
    let pisoAsignado = "";
    if (tipoVehiculo === "auto") {
        pisoAsignado = "Piso 1";
    } else if (tipoVehiculo === "moto") {
        pisoAsignado = "Piso 2";
    } else if (tipoVehiculo === "camion") {
        pisoAsignado = "Piso 3";
    }

    // Mostrar el piso asignado al usuario en el elemento de aviso
    const avisoElement = document.getElementById("aviso");
    avisoElement.textContent = `Piso asignado para ${tipoVehiculo}: ${pisoAsignado}`;
    
    // Aquí puedes continuar con el registro y la gestión del estacionamiento
}
