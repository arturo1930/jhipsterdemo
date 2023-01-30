<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="tarea">
        <h2 class="jh-entity-heading" data-cy="tareaDetailsHeading">
          <span v-text="$t('jhipsterdemoApp.tarea.detail.title')">Tarea</span> {{ tarea.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('jhipsterdemoApp.tarea.nombre')">Nombre</span>
          </dt>
          <dd>
            <span>{{ tarea.nombre }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterdemoApp.tarea.actividades')">Actividades</span>
          </dt>
          <dd>
            <span>{{ tarea.actividades }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterdemoApp.tarea.fechaCreacion')">Fecha Creacion</span>
          </dt>
          <dd>
            <span v-if="tarea.fechaCreacion">{{ $d(Date.parse(tarea.fechaCreacion), 'long') }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterdemoApp.tarea.historiaUsuario')">Historia Usuario</span>
          </dt>
          <dd>
            <div v-if="tarea.historiaUsuario">
              <router-link :to="{ name: 'HistoriaUsuarioView', params: { historiaUsuarioId: tarea.historiaUsuario.id } }">{{
                tarea.historiaUsuario.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="tarea.id" :to="{ name: 'TareaEdit', params: { tareaId: tarea.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./tarea-details.component.ts"></script>
