<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="historiaUsuario">
        <h2 class="jh-entity-heading" data-cy="historiaUsuarioDetailsHeading">
          <span v-text="$t('jhipsterdemoApp.historiaUsuario.detail.title')">HistoriaUsuario</span> {{ historiaUsuario.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('jhipsterdemoApp.historiaUsuario.nombre')">Nombre</span>
          </dt>
          <dd>
            <span>{{ historiaUsuario.nombre }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterdemoApp.historiaUsuario.descripcion')">Descripcion</span>
          </dt>
          <dd>
            <span>{{ historiaUsuario.descripcion }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterdemoApp.historiaUsuario.criteriosAceptacion')">Criterios Aceptacion</span>
          </dt>
          <dd>
            <span>{{ historiaUsuario.criteriosAceptacion }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="historiaUsuario.id"
          :to="{ name: 'HistoriaUsuarioEdit', params: { historiaUsuarioId: historiaUsuario.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./historia-usuario-details.component.ts"></script>
