<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="jhipsterdemoApp.tarea.home.createOrEditLabel"
          data-cy="TareaCreateUpdateHeading"
          v-text="$t('jhipsterdemoApp.tarea.home.createOrEditLabel')"
        >
          Create or edit a Tarea
        </h2>
        <div>
          <div class="form-group" v-if="tarea.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="tarea.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('jhipsterdemoApp.tarea.nombre')" for="tarea-nombre">Nombre</label>
            <input
              type="text"
              class="form-control"
              name="nombre"
              id="tarea-nombre"
              data-cy="nombre"
              :class="{ valid: !$v.tarea.nombre.$invalid, invalid: $v.tarea.nombre.$invalid }"
              v-model="$v.tarea.nombre.$model"
              required
            />
            <div v-if="$v.tarea.nombre.$anyDirty && $v.tarea.nombre.$invalid">
              <small class="form-text text-danger" v-if="!$v.tarea.nombre.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('jhipsterdemoApp.tarea.actividades')" for="tarea-actividades">Actividades</label>
            <input
              type="text"
              class="form-control"
              name="actividades"
              id="tarea-actividades"
              data-cy="actividades"
              :class="{ valid: !$v.tarea.actividades.$invalid, invalid: $v.tarea.actividades.$invalid }"
              v-model="$v.tarea.actividades.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('jhipsterdemoApp.tarea.fechaCreacion')" for="tarea-fechaCreacion"
              >Fecha Creacion</label
            >
            <div class="d-flex">
              <input
                id="tarea-fechaCreacion"
                data-cy="fechaCreacion"
                type="datetime-local"
                class="form-control"
                name="fechaCreacion"
                :class="{ valid: !$v.tarea.fechaCreacion.$invalid, invalid: $v.tarea.fechaCreacion.$invalid }"
                :value="convertDateTimeFromServer($v.tarea.fechaCreacion.$model)"
                @change="updateInstantField('fechaCreacion', $event)"
              />
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('jhipsterdemoApp.tarea.historiaUsuario')" for="tarea-historiaUsuario"
              >Historia Usuario</label
            >
            <select
              class="form-control"
              id="tarea-historiaUsuario"
              data-cy="historiaUsuario"
              name="historiaUsuario"
              v-model="tarea.historiaUsuario"
            >
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  tarea.historiaUsuario && historiaUsuarioOption.id === tarea.historiaUsuario.id
                    ? tarea.historiaUsuario
                    : historiaUsuarioOption
                "
                v-for="historiaUsuarioOption in historiaUsuarios"
                :key="historiaUsuarioOption.id"
              >
                {{ historiaUsuarioOption.descripcion }}
              </option>
            </select>
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
            :disabled="$v.tarea.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./tarea-update.component.ts"></script>
