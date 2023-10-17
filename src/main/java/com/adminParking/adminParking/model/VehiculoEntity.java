    package com.adminParking.adminParking.model;

    import com.fasterxml.jackson.annotation.JsonProperty;

    import jakarta.annotation.Generated;
    import jakarta.persistence.Entity;
    import jakarta.persistence.GeneratedValue;
    import jakarta.persistence.Id;
    import jakarta.persistence.JoinColumn;
    import jakarta.persistence.ManyToOne;
    import jakarta.persistence.OneToOne;
    import jakarta.persistence.Table;

    @Entity
    @Table (name = "tabla_vehiculo")
    public class VehiculoEntity {

        @Id
        @GeneratedValue
        Long id;

        private String tiempoLlegada; 
        private String tiempoSalida;
        private String tipoVehiculo; 
        private String placa; 

        @ManyToOne //Se utiliza para indicar que un vehículo pertenece a un piso 
        private PisoEntity piso; // Agrega la referencia al piso que funciona como una clave foranea

        @ManyToOne
        private TarifaEntity tarifa;


        public VehiculoEntity() {
            // Constructor vacío necesario para JPA
        }

        public VehiculoEntity(String tiempoLlegada, String tiempoSalida, String placa,String tipoVehiculo) {
            this.placa = placa; 
            this.tiempoLlegada = tiempoLlegada;
            this.tiempoSalida = tiempoSalida;
            this.tipoVehiculo = tipoVehiculo;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public void setTiempoLlegada(String tiempoLlegada) {
            this.tiempoLlegada = tiempoLlegada;
        }

        public void setTiempoSalida(String tiempoSalida) {
            this.tiempoSalida = tiempoSalida;
        }

        public Long getId() {
            return id;
        }

        public String getTiempoLlegada() {
            return tiempoLlegada;
        }

        public String getTiempoSalida() {
            return tiempoSalida;
        }


        public void setPlaca(String placa) {
            this.placa = placa;
        }

        public void setTipoVehiculo(String tipoVehiculo) {
            this.tipoVehiculo = tipoVehiculo;
        }

        public String getPlaca() {
            return placa;
        }

        public String getTipoVehiculo() {
            return tipoVehiculo;
        }
        
        public PisoEntity getPiso() {
            return piso;
        }

        public void setPiso(PisoEntity piso) {
            this.piso = piso;
        }

        public void setTarifa(TarifaEntity tarifa) {
            this.tarifa = tarifa;
        }

        public TarifaEntity getTarifa() {
            return tarifa;
        }  
    }