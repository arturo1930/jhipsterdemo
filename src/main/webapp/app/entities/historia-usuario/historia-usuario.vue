<template>
  <div>
    <h2 id="page-heading" data-cy="HistoriaUsuarioHeading">
      <span v-text="$t('jhipsterdemoApp.historiaUsuario.home.title')" id="historia-usuario-heading">Historia Usuarios</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('jhipsterdemoApp.historiaUsuario.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'HistoriaUsuarioCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-historia-usuario"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('jhipsterdemoApp.historiaUsuario.home.createLabel')"> Create a new Historia Usuario </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && historiaUsuarios && historiaUsuarios.length === 0">
      <span v-text="$t('jhipsterdemoApp.historiaUsuario.home.notFound')">No historiaUsuarios found</span>
    </div>
    <div class="table-responsive" v-if="historiaUsuarios && historiaUsuarios.length > 0">
      <table class="table table-striped" aria-describedby="historiaUsuarios">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span v-text="$t('global.field.id')">ID</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('nombre')">
              <span v-text="$t('jhipsterdemoApp.historiaUsuario.nombre')">Nombre</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'nombre'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('descripcion')">
              <span v-text="$t('jhipsterdemoApp.historiaUsuario.descripcion')">Descripcion</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'descripcion'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('criteriosAceptacion')">
              <span v-text="$t('jhipsterdemoApp.historiaUsuario.criteriosAceptacion')">Criterios Aceptacion</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'criteriosAceptacion'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="historiaUsuario in historiaUsuarios" :key="historiaUsuario.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'HistoriaUsuarioView', params: { historiaUsuarioId: historiaUsuario.id } }">{{
                historiaUsuario.id
              }}</router-link>
            </td>
            <td>{{ historiaUsuario.nombre }}</td>
            <td>{{ historiaUsuario.descripcion }}</td>
            <td>{{ historiaUsuario.criteriosAceptacion }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'HistoriaUsuarioView', params: { historiaUsuarioId: historiaUsuario.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'HistoriaUsuarioEdit', params: { historiaUsuarioId: historiaUsuario.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(historiaUsuario)"
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
        ><span
          id="jhipsterdemoApp.historiaUsuario.delete.question"
          data-cy="historiaUsuarioDeleteDialogHeading"
          v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-historiaUsuario-heading" v-text="$t('jhipsterdemoApp.historiaUsuario.delete.question', { id: removeId })">
          Are you sure you want to delete this Historia Usuario?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-historiaUsuario"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeHistoriaUsuario()"
        >
          Delete
        </button>
      </div>
    </b-modal>
    <div v-show="historiaUsuarios && historiaUsuarios.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage" :change="loadPage(page)"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./historia-usuario.component.ts"></script>
