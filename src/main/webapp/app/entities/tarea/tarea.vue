<template>
  <div>
    <h2 id="page-heading" data-cy="TareaHeading">
      <span v-text="$t('jhipsterdemoApp.tarea.home.title')" id="tarea-heading">Tareas</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('jhipsterdemoApp.tarea.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'TareaCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-tarea"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('jhipsterdemoApp.tarea.home.createLabel')"> Create a new Tarea </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && tareas && tareas.length === 0">
      <span v-text="$t('jhipsterdemoApp.tarea.home.notFound')">No tareas found</span>
    </div>
    <div class="table-responsive" v-if="tareas && tareas.length > 0">
      <table class="table table-striped" aria-describedby="tareas">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span v-text="$t('global.field.id')">ID</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('nombre')">
              <span v-text="$t('jhipsterdemoApp.tarea.nombre')">Nombre</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'nombre'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('actividades')">
              <span v-text="$t('jhipsterdemoApp.tarea.actividades')">Actividades</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'actividades'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('fechaCreacion')">
              <span v-text="$t('jhipsterdemoApp.tarea.fechaCreacion')">Fecha Creacion</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'fechaCreacion'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('historiaUsuario.id')">
              <span v-text="$t('jhipsterdemoApp.tarea.historiaUsuario')">Historia Usuario</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'historiaUsuario.id'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="tarea in tareas" :key="tarea.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'TareaView', params: { tareaId: tarea.id } }">{{ tarea.id }}</router-link>
            </td>
            <td>{{ tarea.nombre }}</td>
            <td>{{ tarea.actividades }}</td>
            <td>{{ tarea.fechaCreacion ? $d(Date.parse(tarea.fechaCreacion), 'short') : '' }}</td>
            <td>
              <div v-if="tarea.historiaUsuario">
                <router-link :to="{ name: 'HistoriaUsuarioView', params: { historiaUsuarioId: tarea.historiaUsuario.id } }">{{
                  tarea.historiaUsuario.id
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'TareaView', params: { tareaId: tarea.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'TareaEdit', params: { tareaId: tarea.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(tarea)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="$t('entity.action.delete')">Delete</span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <span slot="modal-title"
        ><span id="jhipsterdemoApp.tarea.delete.question" data-cy="tareaDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-tarea-heading" v-text="$t('jhipsterdemoApp.tarea.delete.question', { id: removeId })">
          Are you sure you want to delete this Tarea?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-tarea"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeTarea()"
        >
          Delete
        </button>
      </div>
    </b-modal>
    <div v-show="tareas && tareas.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage" :change="loadPage(page)"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./tarea.component.ts"></script>
