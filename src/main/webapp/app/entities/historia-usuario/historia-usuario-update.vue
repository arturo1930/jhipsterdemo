<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="jhipsterdemoApp.historiaUsuario.home.createOrEditLabel"
          data-cy="HistoriaUsuarioCreateUpdateHeading"
          v-text="$t('jhipsterdemoApp.historiaUsuario.home.createOrEditLabel')"
        >
          Create or edit a HistoriaUsuario
        </h2>
        <div>
          <div class="form-group" v-if="historiaUsuario.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="historiaUsuario.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('jhipsterdemoApp.historiaUsuario.nombre')" for="historia-usuario-nombre"
              >Nombre</label
            >
            <input
              type="text"
              class="form-control"
              name="nombre"
              id="historia-usuario-nombre"
              data-cy="nombre"
              :class="{ valid: !$v.historiaUsuario.nombre.$invalid, invalid: $v.historiaUsuario.nombre.$invalid }"
              v-model="$v.historiaUsuario.nombre.$model"
              required
            />
            <div v-if="$v.historiaUsuario.nombre.$anyDirty && $v.historiaUsuario.nombre.$invalid">
              <small class="form-text text-danger" v-if="!$v.historiaUsuario.nombre.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('jhipsterdemoApp.historiaUsuario.descripcion')" for="historia-usuario-descripcion"
              >Descripcion</label
            >
            <input
              type="text"
              class="form-control"
              name="descripcion"
              id="historia-usuario-descripcion"
              data-cy="descripcion"
              :class="{ valid: !$v.historiaUsuario.descripcion.$invalid, invalid: $v.historiaUsuario.descripcion.$invalid }"
              v-model="$v.historiaUsuario.descripcion.$model"
              required
            />
            <div v-if="$v.historiaUsuario.descripcion.$anyDirty && $v.historiaUsuario.descripcion.$invalid">
              <small
                class="form-text text-danger"
                v-if="!$v.historiaUsuario.descripcion.required"
                v-text="$t('entity.validation.required')"
              >
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('jhipsterdemoApp.historiaUsuario.criteriosAceptacion')"
              for="historia-usuario-criteriosAceptacion"
              >Criterios Aceptacion</label
            >
            <input
              type="number"
              class="form-control"
              name="criteriosAceptacion"
              id="historia-usuario-criteriosAceptacion"
              data-cy="criteriosAceptacion"
              :class="{ valid: !$v.historiaUsuario.criteriosAceptacion.$invalid, invalid: $v.historiaUsuario.criteriosAceptacion.$invalid }"
              v-model.number="$v.historiaUsuario.criteriosAceptacion.$model"
              required
            />
            <div v-if="$v.historiaUsuario.criteriosAceptacion.$anyDirty && $v.historiaUsuario.criteriosAceptacion.$invalid">
              <small
                class="form-text text-danger"
                v-if="!$v.historiaUsuario.criteriosAceptacion.required"
                v-text="$t('entity.validation.required')"
              >
                This field is required.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.historiaUsuario.criteriosAceptacion.numeric"
                v-text="$t('entity.validation.number')"
              >
                This field should be a number.
              </small>
            </div>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" v-on:click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.cancel')">Cancel</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="$v.historiaUsuario.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./historia-usuario-update.component.ts"></script>
